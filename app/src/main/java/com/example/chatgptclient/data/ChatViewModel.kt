package com.example.chatgptclient.data

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aallam.openai.api.BetaOpenAI
import com.example.chatgptclient.App
import com.example.chatgptclient.data.classes.ChatItem
import com.example.chatgptclient.data.classes.ChatItemDao
import com.example.chatgptclient.repository.BotMessageRepository
import com.example.chatgptclient.ui.theme.LightGreen
import com.example.chatgptclient.ui.theme.LightYellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.chatgptclient.App.Companion.settings
import com.example.chatgptclient.currentBotColorParam
import com.example.chatgptclient.currentUserColorParam
import com.example.chatgptclient.ui.composables.colors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

fun LocalDateTime.getHourAndMinute() =
    this.toString().substringAfter("T").take(5)

class ChatViewModel(
    private val botMessageRepository: BotMessageRepository
) : ViewModel() {

    private val chatItemDao: ChatItemDao

    var currentConversationName by mutableStateOf("")
        private set

    var responseWrittenToConversation = currentConversationName
        private set

    var isConversationCreationDialogVisible by mutableStateOf(false)
        private set

    var botResponseHistory: ArrayList<ChatItem> = ArrayList()
        private set

    var conversationNames = ArrayList<String>()
        private set

    var isResponseBeingConstructed by mutableStateOf(false)
        private set

    var currentBotResponseText by mutableStateOf("")
        private set

    var currentUserText by mutableStateOf("")
        private set

    var currentUserMessageColor by mutableStateOf(LightYellow)
        private set

    var currentBotMessageColor by mutableStateOf(LightGreen)
        private set

    var snackbarHostState by mutableStateOf(SnackbarHostState())

    init {
        Log.d("myLogs", "viewmodel initializer block")

        //constructBotResponse("Who are you?")
        chatItemDao = App.appDatabase.getChatItemDao()



        viewModelScope.launch {
            val addJob = launch { addConversation("default") }
            addJob.join()
            val getConversationJob = launch { getConversationNames() }
            getConversationJob.join()
            getChatHistory("default")
        }

        viewModelScope.launch {
            currentUserMessageColor = colors[getParameterByKey(currentUserColorParam)].second
            currentBotMessageColor = colors[getParameterByKey(currentBotColorParam)].second
        }
    }


    @OptIn(BetaOpenAI::class)
    fun constructBotResponse(
        query: String
    ) {
        if(isResponseBeingConstructed) return

        isResponseBeingConstructed = true
        responseWrittenToConversation = currentConversationName
        viewModelScope.launch {

            val chatUserItem = ChatItem(
                id = 0,
                text = query,
                time = LocalDateTime.now().getHourAndMinute(),
                role = "user",
                conversationName = responseWrittenToConversation
            )

            if(chatUserItem.text.isNotEmpty()) {
                botResponseHistory.add(chatUserItem)

                withContext(Dispatchers.IO) {
                    botMessageRepository.addChatItemToDatabase(chatUserItem)
                }
            }

            val source = botMessageRepository.getChatRecentMessage(query)


            source.collect { chatCompletionChunk ->
                currentBotResponseText +=
                    chatCompletionChunk.choices.get(0).delta?.content ?: ""

                //   Log.d("myLogs", chatCompletionChunk.choices.get(0).delta?.content.toString())
            }


            val chatBotItem = ChatItem(
                id = 0,
                text = currentBotResponseText,
                time = LocalDateTime.now().getHourAndMinute(),
                role = "chat",
                conversationName = responseWrittenToConversation
            )

            if(chatBotItem.text.isNotEmpty()) {
                botResponseHistory.add(chatBotItem)

                withContext(Dispatchers.IO) {
                    botMessageRepository.addChatItemToDatabase(chatBotItem)
                }
            }

            currentBotResponseText = ""
            isResponseBeingConstructed = false
        }
    }

    fun addConversation(conversationName: String){
        if(conversationNames.contains(conversationName))
            return

        conversationNames.add(conversationName)
        conversationNames = conversationNames
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                botMessageRepository.addConversationToDatabase(conversationName)
            }
            getChatHistory(conversationName)
        }

        /*viewModelScope.launch {
            insertConversationJob.join()
            getChatHistory(conversationName)
        }*/
    }

    fun getChatHistory(conversationName: String){
        currentConversationName = conversationName
        botResponseHistory.clear()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for (item in botMessageRepository.getChatHistory(conversationName)) {
                    if(item.text.isNotEmpty()) {
                        botResponseHistory.add(item)
                    }
                }
            }

            currentUserText = "s"
            currentUserText=""
            currentConversationName = conversationName
        }

    }

    fun getConversationNames() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                conversationNames.clear()
                for (item in botMessageRepository.getConversationNames()) {
                    conversationNames.add(item)
                }
            }
        }
    }

    fun setUserText(text: String){
        currentUserText = text
    }

    fun setConversationCreationDialogVisibility(value: Boolean){
        isConversationCreationDialogVisible = value
    }

    fun setUserMessageColor(colorIndex: Int){
        currentUserMessageColor = colors.get(colorIndex).second
        updateSettings(
            preferenceKey = currentUserColorParam,
            value = colorIndex
        )
    }

    fun setBotMessageColor(colorIndex: Int){
        currentBotMessageColor = colors.get(colorIndex).second
        updateSettings(
            preferenceKey = currentBotColorParam,
            value = colorIndex
        )
    }

    fun updateSettings(
        preferenceKey: Preferences.Key<Int>,
        value: Int
    ) {
        viewModelScope.launch {
            settings.edit { preferences ->
                preferences[preferenceKey] = value
            }
            Log.d("myLogs", "updated value $preferenceKey")
        }
    }

    private suspend fun getParameterByKey(
        key: Preferences.Key<Int>
    ) = settings.data.map {preferences ->
            preferences[key] ?: -1
        }
        .first {value -> value > -1}

    companion object {
        val Factory = viewModelFactory{
            initializer {
                val botMessageRepository = appContainer.botMessageRepository
                ChatViewModel(botMessageRepository)
            }
        }
    }
}