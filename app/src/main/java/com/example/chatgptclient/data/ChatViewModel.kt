package com.example.chatgptclient.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aallam.openai.api.BetaOpenAI
import com.example.chatgptclient.data.classes.ChatItem
import com.example.chatgptclient.repository.BotMessageRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val botMessageRepository: BotMessageRepository
) : ViewModel() {

    var botResponseHistory: ArrayList<ChatItem> = ArrayList()
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

    }

    @OptIn(BetaOpenAI::class)
    fun constructBotResponse(
        query: String
    ) {
        if(isResponseBeingConstructed) return

        viewModelScope.launch {
            isResponseBeingConstructed = true
            botResponseHistory.add(
                ChatItem(
                    text = query
                )
            )
            val source = botMessageRepository.getChatRecentMessage(query)

            try {
                source.collect {chatCompletionChunk ->
                    currentBotResponseText +=
                        chatCompletionChunk.choices.get(0).delta?.content ?: ""

                    Log.d("myLogs", chatCompletionChunk.choices.get(0).delta?.content.toString())
                }
            } catch (e: Exception) {
                Log.d("myLogs", e.message ?: "")
            }

            botResponseHistory.add(
                ChatItem(
                    currentBotResponseText
                )
            )
            currentBotResponseText = ""
            isResponseBeingConstructed = false
        }
    }

    fun setUserText(text: String){
        currentUserText = text
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