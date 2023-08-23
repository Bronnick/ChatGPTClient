package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatgptclient.data.ChatViewModel
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(

) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val chatViewModel: ChatViewModel =
        viewModel(factory = ChatViewModel.Factory)

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),

        scaffoldState = scaffoldState,

        drawerContent = {
            ConversationSelector(
                conversationNames = chatViewModel.conversationNames,
                currentConversationName = chatViewModel.currentConversationName,
                onFloatingActionButtonClick = {
                    chatViewModel.setConversationCreationDialogVisibility(true)
                },
                onConversationChange = {conversationName ->
                    chatViewModel.getChatHistory(conversationName)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },

        topBar = {
            ChatBoxTopBar (
                currentConversationName = chatViewModel.currentConversationName,
                onClickMenuButton = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        }
    ) {
        ChatBox(
            modifier = Modifier.padding(it),
            viewModel = chatViewModel,
            onClickSendRequestButton = { value ->

                if(chatViewModel.currentUserText.isNotEmpty()) {
                    chatViewModel.constructBotResponse(value)
                    keyboardController?.hide()
                } else{
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Your request is empty.",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                chatViewModel.setUserText("")
            }
        )
    }
}