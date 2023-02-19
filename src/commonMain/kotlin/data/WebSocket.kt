package data

import kotlinx.coroutines.flow.Flow
import model.messages.WebsocketRequest
import model.messages.WebsocketResponse

interface WebSocket {
    suspend fun observerResponses(): Flow<WebsocketResponse>
    suspend fun sendRequest(request: WebsocketRequest)
}
