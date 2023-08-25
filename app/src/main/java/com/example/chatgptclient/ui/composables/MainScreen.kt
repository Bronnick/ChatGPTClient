package com.example.chatgptclient.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatgptclient.R
import com.example.chatgptclient.data.ChatViewModel
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


sealed class Screen(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourceId: Int
){
    object Home : Screen(
        route ="home",
        icon = Icons.Filled.Email,
        resourceId = R.string.chat
    )
    object Settings : Screen(
        route = "settings",
        icon = Icons.Filled.Settings,
        resourceId = R.string.settings
    )
}

val items = listOf(
    Screen.Home,
    Screen.Settings
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(

) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val chatViewModel: ChatViewModel =
        viewModel(factory = ChatViewModel.Factory)

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),

        scaffoldState = scaffoldState,

        drawerContent = {
            ConversationSelector(
                conversationNames = chatViewModel.conversationNames,
                currentConversationName = chatViewModel.currentConversationName,
                onFloatingActionButtonClick = {
                    chatViewModel.setConversationCreationDialogVisibility(true)
                },
                onConversationChange = {conversationName ->
                    chatViewModel.getChatHistory(conversationName)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },

        topBar = {
            if(currentDestination?.route == "home") {
                ChatBoxTopBar(
                    currentConversationName = chatViewModel.currentConversationName,
                    onClickMenuButton = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                )
            }
        },

        bottomBar = {
            BottomNavigation {

                items.forEach{screen ->
                    BottomNavigationItem(
                        icon = {Icon(screen.icon, contentDescription = null)},
                        label = {Text(text = stringResource(screen.resourceId))},
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState  = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(it)
        ) {
            composable(Screen.Home.route) {
                ChatBox(
                    modifier = Modifier,
                    viewModel = chatViewModel,
                    onClickSendRequestButton = { value ->

                        if(chatViewModel.currentUserText.isNotEmpty()) {
                            chatViewModel.constructBotResponse(value)
                            keyboardController?.hide()
                        } else{
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Your request is empty.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        chatViewModel.setUserText("")
                    }
                )
            }
            composable(Screen.Settings.route){
                SettingsScreen()
            }
        }
    }
}