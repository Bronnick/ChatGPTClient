package com.example.chatgptclient.data

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aallam.openai.api.BetaOpenAI
import com.example.chatgptclient.App
import com.example.chatgptclient.data.classes.ChatItem
import com.example.chatgptclient.data.classes.ChatItemDao
import com.example.chatgptclient.repository.BotMessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

fun LocalDateTime.getHourAndMinute() =
    this.toString().substringAfter("T").take(5)

class ChatViewModel(
    private val botMessageRepository: BotMessageRepository
) : ViewModel() {

    private val chatItemDao: ChatItemDao

    var currentConversationName by mutableStateOf("default")
        private set

    var responseWrittenToConversation = currentConversationName
        private set

    var isConversationSelectorVisible by mutableStateOf(true)
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

    var snackbarHostState by mutableStateOf(SnackbarHostState())

    var scrollState by mutableStateOf(0)
        private set

    init{
        Log.d("myLogs", "viewmodel initializer block")

        //constructBotResponse("Who are you?")
        chatItemDao = App.appDatabase.getChatItemDao()
        getChatHistory("default")
        getConversationNames()
    }

    @OptIn(BetaOpenAI::class)
    fun constructBotResponse(
        query: String
    ) {
        if(isResponseBeingConstructed) return

        viewModelScope.launch {
            isResponseBeingConstructed = true

            responseWrittenToConversation = currentConversationName

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

            try {
                source.collect { chatCompletionChunk ->
                    currentBotResponseText +=
                        chatCompletionChunk.choices.get(0).delta?.content ?: ""

                 //   Log.d("myLogs", chatCompletionChunk.choices.get(0).delta?.content.toString())
                }
            } catch (e: Exception) {
                Log.d("myLogs", e.message ?: "")
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
        Log.d("myLogs", "getting chat history")

        viewModelScope.launch {
            Log.d("myLogs", "launched coroutine getting history")
            withContext(Dispatchers.IO) {
                for (item in botMessageRepository.getChatHistory(conversationName)) {
                    if(item.text.isNotEmpty()) {
                        botResponseHistory.add(item)
                    }
                }

                    Log.d("myLogs", "ended getting history")
            }
            for(item in botResponseHistory)
                Log.d("myLogs", item.toString())
            currentUserText = "s"
            currentUserText=""
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

    fun setConversationSelectorVisibility(value: Boolean){
        isConversationSelectorVisible = value
    }

    fun setConversationCreationDialogVisibility(value: Boolean){
        isConversationCreationDialogVisible = value
    }

    companion object {
        val Factory = viewModelFactory{
            initializer {
                Log.d("myLogs", "viewmodel creation")
                val botMessageRepository = appContainer.botMessageRepository
                ChatViewModel(botMessageRepository)
            }
        }
    }
}