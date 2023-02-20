package data.websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.util.logging.*
import io.ktor.util.reflect.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.serialization.InternalSerializationApi
import model.messages.WebsocketRequest
import model.messages.WebsocketResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class WebSocketImpl(val client: HttpClient, private val host: String = "localhost") : WebSocket {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var connection: DefaultClientWebSocketSession? = null
    private var connectingJob: Deferred<*>? = null
    private val channels = mutableMapOf<KClass<*>, Channel<WebsocketResponse>>()
    init {
        connectingJob = scope.async {
            connection = client.webSocketSession(
                host = host,
                path = "/live",
            )
            scope.launch {
                connection!!.incoming
                    .receiveAsFlow()
                    .mapNotNull { frame ->
                        if (frame is Frame.Text) {
                            Json.decodeFromString(WebsocketResponse.serializer(), frame.readText())
                        } else {
                            null
                        }
                    }
                    .collect { element ->
                        val channel = channels.entries.firstOrNull{ element.instanceOf(it.key) }?.value ?: return@collect
                        channel.send(element)
                    }
            }
        }
    }

    override fun <T: WebsocketResponse>observeResponses(clazz: KClass<T>, connectMessage: WebsocketRequest?): Flow<T>{
        val channel = channels.getOrPut(clazz){ Channel() } as Channel<T>
        val flow = channel.receiveAsFlow()
        if(connectMessage != null) {
            scope.launch { sendRequest(connectMessage) }
        }
        return flow
    }


    @OptIn(InternalSerializationApi::class)
    override suspend fun sendRequest(request: WebsocketRequest) {
        connectingJob!!.await()
        val json = Json.encodeToString(WebsocketRequest::class.serializer(), request)
        connection!!.send(json)
    }
}