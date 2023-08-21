package com.example.chatgptclient.data

import android.util.Log
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

    var currentConversationName by mutableStateOf("")
        private set

    var isConversationSelectorVisible by mutableStateOf(true)
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

            val chatUserItem = ChatItem(
                id = 0,
                text = query,
                time = LocalDateTime.now().getHourAndMinute(),
                role = "user",
                conversationName = "default"
            )

            botResponseHistory.add(chatUserItem)

            withContext(Dispatchers.IO) {
                botMessageRepository.addChatItemToDatabase(chatUserItem)
            }

            val source = botMessageRepository.getChatRecentMessage(query)

            try {
                source.collect { chatCompletionChunk ->
                    currentBotResponseText +=
                        chatCompletionChunk.choices.get(0).delta?.content ?: ""

                    Log.d("myLogs", chatCompletionChunk.choices.get(0).delta?.content.toString())
                }
            } catch (e: Exception) {
                Log.d("myLogs", e.message ?: "")
            }

            val chatBotItem = ChatItem(
                id = 0,
                text = currentBotResponseText,
                time = LocalDateTime.now().getHourAndMinute(),
                role = "chat",
                conversationName = "default"
            )


            botResponseHistory.add(chatBotItem)

            withContext(Dispatchers.IO) {
                botMessageRepository.addChatItemToDatabase(chatBotItem)
            }

            currentBotResponseText = ""
            isResponseBeingConstructed = false
        }
    }

    fun getChatHistory(conversationName: String){
        currentConversationName = conversationName
        botResponseHistory.clear()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for (item in botMessageRepository.getChatHistory(conversationName)) {
                    botResponseHistory.add(item)
                }
            }
        }
    }

    fun getConversationNames() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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