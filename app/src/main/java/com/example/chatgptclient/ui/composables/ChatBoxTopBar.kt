package com.example.chatgptclient.ui.composables


import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun ChatBoxTopBar(
    currentConversationName: String,
    onClickMenuButton: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onClickMenuButton
        ) {
            Icon(
                modifier = Modifier
                    .widthIn(min = 50.dp),
                imageVector = Icons.Filled.Menu,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = currentConversationName,
            textAlign = TextAlign.Center,
        )
    }
}