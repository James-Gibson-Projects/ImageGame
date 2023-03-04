package model.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed class GameRequest: WebsocketRequest(){
    abstract val gameId: String
    @Serializable
    data class Refresh(override val gameId: String): GameRequest()

    @Serializable
    data class MovePiece(override val gameId: String, val from: Vec2, val to: Vec2): GameRequest()
}

@Serializable
data class GameResponse(val state: ChessBoard, val moves: List<PieceMoves>, val playingAs: Color): WebsocketResponse()

@Serializable
data class PieceMoves(val piece: Vec2, val moves: List<Vec2>)