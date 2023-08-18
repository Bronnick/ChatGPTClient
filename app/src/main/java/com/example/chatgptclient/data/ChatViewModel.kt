package com.example.chatgptclient.data

import android.text.Editable.Factory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.aallam.openai.api.BetaOpenAI
import com.example.chatgptclient.repository.BotMessageRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ChatViewModel(
    private val botMessageRepository: BotMessageRepository
) : ViewModel() {
    var currentBotResponse by mutableStateOf("")

    init{
        Log.d("myLogs", "viewmodel initializer block")
        viewModelScope.launch {
            constructBotResponse("Who are you?")
        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun constructBotResponse(
        query: String
    ) {
        currentBotResponse = "bonker"
        val source = botMessageRepository.getChatRecentMessage(query)

        try {
            source.collect {
                currentBotResponse +=
                    it.choices.get(0).delta?.content.toString()

                Log.d("myLogs", it.choices.get(0).delta?.content.toString())
            }
        } catch(e: Exception){
            Log.d("myLogs", e.message ?: "")
        }
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