package domain.repo

interface GameRepo {
    fun createGame(whiteUsername: String, blackUsername: String)
    fun getGameState(gameId: String): ChessBoard

}