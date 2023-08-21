package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.chatgptclient.data.ChatViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatBox(
    modifier: Modifier,
    viewModel: ChatViewModel
) {

    val userText = viewModel.currentUserText
    val botResponseHistory = viewModel.botResponseHistory
    val chatResponse = viewModel.currentBotResponseText

    val keyboardController = LocalSoftwareKeyboardController.current


    Box(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 4.dp)
                    .weight(0.5f, true)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom
            ) {
                ProvideTextStyle(
                    TextStyle(color = Color.Black)
                ) {
                    for (item in botResponseHistory) {
                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
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
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth(),
                        text = chatResponse,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .background(color = Color.Black),

                verticalAlignment = Alignment.Bottom
            ) {
                ProvideTextStyle(
                    TextStyle(color = Color.White)
                ) {
                    TextField(
                        value = userText,
                        onValueChange = {
                            viewModel.setUserText(it)
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
                        viewModel.constructBotResponse(userText)
                        viewModel.setUserText("")
                        keyboardController?.hide()
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

    if(viewModel.isConversationCreationDialogVisible){
        ConversationCreationDialog(
            onDismissRequest = {
                viewModel.setConversationCreationDialogVisibility(false)
            },
            onCreateButtonClick = {conversationName ->
                viewModel.addConversation(conversationName)
                viewModel.setConversationCreationDialogVisibility(false)
            }
        )
    }

}