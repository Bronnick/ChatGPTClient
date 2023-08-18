package com.example.chatgptclient.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow

class BotMessageRepository @OptIn(BetaOpenAI::class) constructor(
    private val openAI: OpenAI
) {

    @OptIn(BetaOpenAI::class)
    fun getChatRecentMessage(
        userMessage: String
    ): Flow<ChatCompletionChunk> = openAI.chatCompletions(ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = userMessage
                )
            )
        )
    )

}