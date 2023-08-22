package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConversationSelector(
    conversationNames: List<String>,
    currentConversationName: String,
    onFloatingActionButtonClick: () -> Unit,
    onConversationChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .verticalScroll(ScrollState(0)),
    ) {

        for (item in conversationNames) {
            ConversationDrawerItem(
                conversationName = item,
                currentConversationName = currentConversationName,
                onConversationChange = onConversationChange
            )
        }

        Box(
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                modifier = Modifier,
                onClick = onFloatingActionButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    }
}