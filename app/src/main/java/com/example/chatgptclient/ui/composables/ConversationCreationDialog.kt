package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*


@Composable
fun ConversationCreationDialog(
    onDismissRequest: () -> Unit,
    onCreateButtonClick: (String) -> Unit
) {
    var textFieldValue by remember{ mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("Set name for your conversation")
        },

        buttons = {
            Column {
                TextField(
                    value = textFieldValue,
                    onValueChange = {value ->
                        textFieldValue = value
                    }
                )

                Button(
                    onClick = {
                        onCreateButtonClick(textFieldValue)
                    }
                ) {
                    Text(text = "Create")
                }
            }
        }
    )

}