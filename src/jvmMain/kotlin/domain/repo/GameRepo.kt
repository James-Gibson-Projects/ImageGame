package domain.repo

import model.messages.ChessBoard
import model.messages.Color

interface GameRepo {
    fun createGame(whiteUsername: String, blackUsername: String): String
    fun getGameState(gameId: String): ChessBoard
    fun setGameState(gameId: String, game: ChessBoard)
    fun getColor(gameId: String, username: String): Color
    fun getGames(username: String): List<String>
}