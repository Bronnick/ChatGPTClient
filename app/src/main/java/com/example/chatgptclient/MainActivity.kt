package com.example.chatgptclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.example.chatgptclient.ui.composables.ChatBox
import com.example.chatgptclient.ui.composables.MainScreen
import com.example.chatgptclient.ui.theme.ChatGPTClientTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/*interface ChatGPTService{

    @GET("completions.json")
    suspend fun getResponse(

    ): Error
}*/

class MainActivity : ComponentActivity() {
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    @OptIn(BetaOpenAI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiKey = "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"

        /*val baseUrl = "https://api.openai.com/v1/chat/"

        scope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitService = retrofit.create<ChatGPTService>()
            val response = retrofitService.getResponse()
                .message
            Log.d("myLogs", response.toString())
        }*/

        setContent {
            ChatGPTClientTheme {
                // A surface container using the 'background' color from the theme
                    MainScreen()

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatGPTClientTheme {
        Greeting("Android")
    }
}