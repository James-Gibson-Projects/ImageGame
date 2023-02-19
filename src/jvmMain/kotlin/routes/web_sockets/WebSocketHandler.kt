package routes.web_sockets

import domain.model.UserSession
import io.ktor.server.websocket.*
import model.messages.WebsocketRequest

interface WebSocketHandler {
    suspend fun shouldHandle(request: WebsocketRequest): Boolean
    suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession)
    suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest)
    suspend fun DefaultWebSocketServerSession.onClose(session: UserSession)
}