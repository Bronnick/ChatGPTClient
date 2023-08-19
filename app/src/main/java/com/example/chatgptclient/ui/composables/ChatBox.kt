package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatgptclient.data.ChatViewModel
import com.example.chatgptclient.ui.helpers.bottomBorder

@Composable
fun ChatBox() {
    val chatViewModel: ChatViewModel =
        viewModel(factory = ChatViewModel.Factory)

    val userText = chatViewModel.currentUserText
    val botResponseHistory = chatViewModel.botResponseHistory
    val chatResponse = chatViewModel.currentBotResponseText

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f, true)
                .verticalScroll(rememberScrollState())
        ) {
            for (item in botResponseHistory) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val borderSize = 1.dp
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = borderSize.toPx()
                            )
                        },
                    text = item.text
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = chatResponse,
            )
        }

        Row(
            modifier = Modifier.
                weight(0.2f, true),
            verticalAlignment = Alignment.Bottom
        ) {
            ProvideTextStyle(TextStyle(color = Color.White)) {
                TextField(
                    value = userText,
                    onValueChange = {
                        chatViewModel.setUserText(it)
                    },
                    modifier = Modifier
                        .background(
                            color = Color.Black
                        )
                        .weight(0.9f)
                )
            }
            IconButton(
                onClick = {
                    if(!chatViewModel.responseIsBeingConstructed)
                        chatViewModel.constructBotResponse(userText)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = null
                )
            }
        }
    }
}