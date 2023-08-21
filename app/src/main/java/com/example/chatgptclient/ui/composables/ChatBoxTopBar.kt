package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun ChatBoxTopBar(
    currentConversationName: String,
    onClickMenuButton: () -> Unit
) {
    IconButton(
        onClick = onClickMenuButton
    ) {
        Row() {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null
            )

            Text(
                text = currentConversationName
            )
        }
    }
}