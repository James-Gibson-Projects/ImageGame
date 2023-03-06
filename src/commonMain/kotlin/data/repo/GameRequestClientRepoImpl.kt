package data.repo

import data.websocket.WebSocket
import kotlinx.coroutines.flow.Flow
import model.messages.GameRequest
import model.messages.GameResponse
import model.messages.Vec2
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameRequestClientRepoImpl : GameRequestClientRepo, KoinComponent {
    private val ws: WebSocket by inject()
    override fun observeGameState(): Flow<GameResponse> {
        return ws.observeResponses(GameResponse::class)
    }

    override suspend fun refresh(gameId: String) {
        ws.sendRequest(GameRequest.Refresh(gameId))
    }

    override suspend fun movePiece(gameId: String, from: Vec2, to: Vec2) {
        ws.sendRequest(GameRequest.MovePiece(gameId, from, to))
    }
}