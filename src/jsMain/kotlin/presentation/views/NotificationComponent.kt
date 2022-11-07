package presentation.views

import androidx.compose.runtime.*
import data.defaultClient
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun NotificationComponent() {
    val mainScope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf<List<String>>(listOf()) }
    var session: DefaultClientWebSocketSession? by remember { mutableStateOf(null) }
    remember {
        mainScope.launch {
            session = defaultClient.webSocketSession(
                method = HttpMethod.Get,
                path = "/chat"
            )
        }
    }
    if (session == null) {
        Text("Session not found")
    } else {
        remember {
            mainScope.launch {
                while (true) {
                    for (frame in session!!.incoming) {
                        if(frame is Frame.Text) messages = messages + listOf(frame.readText())
                    }
                }

            }
        }
    }
    TextInput(message) {
        onInput { message = it.value }
    }
    messages.forEach {
        Text(it)
        Br()
    }
    Br()
    Button(
        attrs = {
            onClick {
                mainScope.launch {
                    (session ?: throw NullPointerException("Session not found")).send(message)
                }.invokeOnCompletion { message = "" }
            }
        }
    ) {
        Text("Send")
    }
}
