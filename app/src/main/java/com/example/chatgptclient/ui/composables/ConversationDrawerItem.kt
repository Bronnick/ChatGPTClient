package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun ConversationDrawerItem(
    conversationName: String,
    currentConversationName: String,
    onConversationChange: (String) -> Unit
) {
    ProvideTextStyle(
        TextStyle(color = Color.Black)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = if (currentConversationName == conversationName) {
                        Color.Cyan
                    } else Color.White
                )
                .drawBehind {
                    val borderSize = 1.dp
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = borderSize.toPx()
                    )
                }
                .padding(4.dp)
                .clickable {
                    onConversationChange(conversationName)
                },
            text = conversationName
        )

    }
}