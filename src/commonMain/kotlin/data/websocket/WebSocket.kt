package data.websocket

import kotlinx.coroutines.flow.Flow
import model.messages.WebsocketRequest
import model.messages.WebsocketResponse
import kotlin.reflect.KClass

interface WebSocket {
    fun <T: WebsocketResponse>observeResponses(clazz: KClass<T>, connectMessage: WebsocketRequest? = null): Flow<T>
    suspend fun sendRequest(request: WebsocketRequest)
}
