package net.malkowscy.application.domain.repo

import data.db.schema.Piece
import data.db.schema.Vector2
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope

interface GameRepo {
    fun createGame(): Long
    fun addUserToGame(username: String, gameId: Long, color: Piece.Color)
    fun QueryScope.addBoardPiece(gameId: Long, position: Vector2, pieceData: Piece): EmptyResult
}