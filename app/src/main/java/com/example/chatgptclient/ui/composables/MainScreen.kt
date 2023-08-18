package com.example.chatgptclient.ui.composables

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen(
    text: String
) {
    val scope = rememberCoroutineScope()


    var textValue by remember{ mutableStateOf("") }
    var response by remember {
        mutableStateOf("")
    }
    TextField(
        value = textValue,
        onValueChange = {textValue = it}
    )

    Text(
        text = text
    )
}