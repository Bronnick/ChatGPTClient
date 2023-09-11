package com.example.chatgptclient.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aallam.openai.api.chat.ChatRole
import com.example.chatgptclient.data.ChatViewModel
import com.example.chatgptclient.ui.theme.LightBlue
import com.example.chatgptclient.ui.theme.LightGreen
import com.example.chatgptclient.ui.theme.LightPink
import com.example.chatgptclient.ui.theme.LightYellow

enum class Role{
    USER,
    BOT
}

val colors = listOf(
    "LightYellow" to LightYellow,
    "LightGreen" to LightGreen,
    "LightPink" to LightPink,
    "LightBlue" to LightBlue,
    "Red" to Color.Red,
    "LightGray" to Color.LightGray,
    "Green" to Color.Green,
    "White" to Color.White
)

@Composable
fun SettingsScreen(
    viewModel: ChatViewModel
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Settings",
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )

        MessageColorSelector(
            viewModel = viewModel,
            role = Role.USER
        )

        MessageColorSelector(
            viewModel = viewModel,
            role = Role.BOT
        )
    }
}

@Composable
fun MessageColorSelector(
    viewModel: ChatViewModel,
    role: Role
){

    val (selectedColor, onColorSelected) = remember {
        mutableStateOf(
            if(role == Role.USER)
                viewModel.currentUserMessageColor
            else viewModel.currentBotMessageColor
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 20.dp,
                start = 8.dp,
                end = 8.dp,
                top = 8.dp
            ),
    ) {

        Text(
            text = if(role == Role.USER) "User messages сolor"
                else "Bot messages color",
            fontSize = 20.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(8.dp)
                .background(color = selectedColor)
        )

        Column(
            modifier = Modifier
                .selectableGroup(),
        ) {
            colors.forEach { (colorName, color)  ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (color == selectedColor),
                        onClick = {
                            onColorSelected(color)

                            if(role == Role.USER)
                                viewModel.setUserMessageColor(colors.indexOf(Pair(colorName,color)))

                            else viewModel.setBotMessageColor(colors.indexOf(Pair(colorName,color)))
                        }
                    )
                    Text(text = colorName)
                }
            }
        }
    }
}