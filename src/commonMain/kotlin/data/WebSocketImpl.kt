package data

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import model.messages.WebsocketRequest
import model.messages.WebsocketResponse
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json

class WebSocketImpl(val client: HttpClient, private val host: String = "192.168.1.118") : WebSocket {

    private var connection: DefaultClientWebSocketSession? = null
    private var connectingJob: Deferred<*>? = null
    init {
        connectingJob = CoroutineScope(Dispatchers.Default).async {
            connection = client.webSocketSession(
                host = host,
                path = "/live"
            )
        }
    }

    override suspend fun observerResponses(): Flow<WebsocketResponse>{
        connectingJob!!.await()
        return connection!!.incoming
            .receiveAsFlow()
            .map { with(connection!!){
                val text = (it as Frame.Text).readText()
                Json.decodeFromString(WebsocketResponse.serializer(), text)
            }}
    }


    override suspend fun sendRequest(request: WebsocketRequest) {
        if(connection == null) throw Exception("You must call connect on WebSocket before observing responses")
        else connection!!.sendSerialized(request)
    }
}