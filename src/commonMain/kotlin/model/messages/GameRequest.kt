package model.messages

import kotlinx.serialization.Serializable


@Serializable
sealed class GameRequest: WebsocketRequest(){
    abstract val gameId: String
    @Serializable
    data class Refresh(override val gameId: String): GameRequest()

    @Serializable
    data class MovePiece(override val gameId: String, val from: Vec2, val to: Vec2): GameRequest()
}