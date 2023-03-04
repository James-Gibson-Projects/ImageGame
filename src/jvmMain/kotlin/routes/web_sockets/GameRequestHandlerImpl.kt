package routes.web_sockets

import domain.model.UserSession
import domain.repo.GameRequestRepo
import io.ktor.server.websocket.*
import model.messages.GameInviteRequest
import model.messages.GameInviteResponse
import model.messages.WebsocketRequest
import model.messages.WebsocketResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameRequestHandlerImpl : GameRequestHandler, KoinComponent {
    val repo: GameRequestRepo by inject()
    override suspend fun shouldHandle(request: WebsocketRequest): Boolean {
        return request is GameInviteRequest
    }

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) {
    }

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
        request as GameInviteRequest
        when(request){
            is GameInviteRequest.Send -> {
                repo.sendRequest(session.username, request.to)
                connections[request.to]!!.webSocketSession.sendSerialized<WebsocketResponse>(GameInviteResponse.Received(request.to))
            }
            is GameInviteRequest.Accept -> {
                val gameId = repo.acceptRequest(request.from, session.username)
                sendSerialized<WebsocketResponse>(GameInviteResponse.Started(gameId))
            }
        }
    }

    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) {}
}