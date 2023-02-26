package routes.web_sockets

import domain.model.UserSession
import io.ktor.server.websocket.*
import model.messages.WebsocketRequest

class GameWebsocketHandler: WebSocketHandler {
    override suspend fun shouldHandle(request: WebsocketRequest) = request is GameRequest

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) {
        TODO("Not yet implemented")
    }

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) {
        TODO("Not yet implemented")
    }
}