package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.GameResponse
import model.messages.Vec2

interface GameRequestClientRepo {
    fun observeGameState(): Flow<GameResponse>
    suspend fun refresh(gameId: String)
    suspend fun movePiece(gameId: String, from: Vec2, to: Vec2)
}