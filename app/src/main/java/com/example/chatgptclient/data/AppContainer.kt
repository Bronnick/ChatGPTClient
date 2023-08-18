package com.example.chatgptclient.data

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.chatgptclient.repository.BotMessageRepository
import kotlin.time.Duration.Companion.seconds

val appContainer by lazy{
    AppContainer()
}

class AppContainer {
    private val apiKey = "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"

    private val openAI = OpenAI(
        token = apiKey,
        timeout = Timeout(socket = 60.seconds),
    )

    val botMessageRepository by lazy{
        BotMessageRepository(openAI)
    }
}