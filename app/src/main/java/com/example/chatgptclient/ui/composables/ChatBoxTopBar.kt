package com.example.chatgptclient.ui.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun ChatBoxTopBar(
    onClickMenuButton: () -> Unit
) {
    IconButton(
        onClick = onClickMenuButton
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = null
        )
    }
}