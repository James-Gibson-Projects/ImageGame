package domain.repo

import model.messages.ChessBoard

interface GameRepo {
    fun createGame(whiteUsername: String, blackUsername: String): String
    fun getGameState(gameId: String): ChessBoard
    fun setGameState(gameId: String, game: ChessBoard)
    fun getGames(username: String): List<String>
}