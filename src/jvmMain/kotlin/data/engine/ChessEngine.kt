package data.engine

interface ChessEngine {
    suspend fun getMove(fen: String, allowedTimeMs: Int = 100): String
}