package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatgptclient.data.ChatViewModel
import com.example.chatgptclient.ui.theme.LightGreen
import com.example.chatgptclient.ui.theme.LightYellow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatBox(
    modifier: Modifier,
    viewModel: ChatViewModel,
    onClickSendRequestButton: (String) -> Unit
) {

    val userText = viewModel.currentUserText
    val botResponseHistory = viewModel.botResponseHistory
    val chatResponse = viewModel.currentBotResponseText

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit){
        scrollState.animateScrollTo(
            scrollState.maxValue
        )
    }

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
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Bottom
            ) {
                ProvideTextStyle(
                    TextStyle(color = Color.Black)
                ) {
                    for (item in botResponseHistory) {
                        Column(

                        ) {
                            if(item.role == "user") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 30.dp),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    Text(
                                        text = item.time,
                                        textAlign = TextAlign.End,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if(item.conversationName ==
                                viewModel.currentConversationName
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color =
                                            if (item.role == "user") viewModel.currentUserMessageColor
                                            else viewModel.currentBotMessageColor
                                        )
                                        .drawBehind {
                                            val borderSize = 1.dp
                                            drawLine(
                                                color = Color.Black,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = borderSize.toPx(),
                                                pathEffect = PathEffect.cornerPathEffect(5.0f)
                                            )
                                        }
                                        .padding(
                                            bottom = 8.dp,
                                            top = 8.dp,
                                            start = 4.dp,
                                            end = 4.dp
                                        ),
                                    text = item.text
                                )
                            }
                        }
                    }

                    if(viewModel.currentConversationName ==
                            viewModel.responseWrittenToConversation) {
                        Text(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .fillMaxWidth(),
                            text = chatResponse,
                        )
                    }
                }
            }

            ProvideTextStyle(
                TextStyle(color = Color.White)
            ) {
                SnackbarHost(
                    hostState = viewModel.snackbarHostState
                )
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
                        onClickSendRequestButton(viewModel.currentUserText)
                        /*viewModel.constructBotResponse(userText)

                        if(viewModel.currentUserText.isNotEmpty())
                            keyboardController?.hide()

                        viewModel.setUserText("")*/

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