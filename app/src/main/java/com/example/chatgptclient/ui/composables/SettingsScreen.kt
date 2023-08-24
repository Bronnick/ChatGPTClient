package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    Column{
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "settings",
            fontSize = 30.sp
        )
    }
}