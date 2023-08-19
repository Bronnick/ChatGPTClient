package com.example.chatgptclient.ui.helpers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.bottomBorder() {
    drawBehind {
        val borderSize = 1.dp
        drawLine(
            color = Color.White,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = borderSize.toPx()
        )
    }
}