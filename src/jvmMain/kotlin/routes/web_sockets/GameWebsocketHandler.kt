package routes.web_sockets

import domain.model.UserSession
import domain.repo.GameRepo
import io.ktor.server.websocket.*
import model.messages.GameRequest
import model.messages.WebsocketRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameWebsocketHandler: WebSocketHandler, KoinComponent{
    val gameRepo: GameRepo by inject()
    override suspend fun shouldHandle(request: WebsocketRequest) = request is GameRequest

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) {
    }

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
        request as GameRequest
        when(request){
            is GameRequest.Refresh -> {

            }
        }
    }

    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) {
        TODO("Not yet implemented")
    }
}