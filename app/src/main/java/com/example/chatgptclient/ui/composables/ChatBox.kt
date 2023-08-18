package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatgptclient.data.ChatViewModel

@Composable
fun ChatBox() {
    val chatViewModel: ChatViewModel =
        viewModel(factory = ChatViewModel.Factory)

    val chatResponse = chatViewModel.currentBotResponse

    Text(
        modifier = Modifier.fillMaxSize(),
        text = chatResponse,
    )
}