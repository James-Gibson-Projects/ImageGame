package data.repo

import data.websocket.WebSocket
import kotlinx.coroutines.flow.Flow
import model.messages.GameInviteRequest
import model.messages.GameInviteResponse
import org.koin.core.component.KoinComponent

class GameInviteRequestClientRepoImpl(private val webSocket: WebSocket): GameInviteRequestClientRepo, KoinComponent {
    override fun observeInvites(): Flow<GameInviteResponse> {
        return webSocket.observeResponses(GameInviteResponse::class)
    }

    override suspend fun sendRequest(to: String) {
        webSocket.sendRequest(GameInviteRequest.Send(to))
    }

    override suspend fun acceptRequest(from: String) {
        webSocket.sendRequest(GameInviteRequest.Accept(from))
    }
}