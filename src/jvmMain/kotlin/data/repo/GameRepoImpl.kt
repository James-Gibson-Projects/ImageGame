package data.repo

import data.db.Neo4jDatabase
import domain.repo.GameRepo
import model.messages.ChessBoard
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.core.Graph
import data.db.queries.*
import model.messages.Color
import model.messages.Piece

class GameRepoImpl : GameRepo, KoinComponent{
    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun createGame(whiteUsername: String, blackUsername: String): String {
        return graph.createGame(whiteUsername, blackUsername).first()
    }

    override fun getGameState(gameId: String): ChessBoard {
        return graph.getGame(gameId).first()
    }

    override fun setGameState(gameId: String, game: ChessBoard) {
        graph.setGame(gameId, game.map { _, it -> Piece.encode(it).toLong() })
    }

    override fun getColor(gameId: String, username: String): Color {
        return if(graph.getIsPlayingAsWhite(gameId, username).first()) Color.White else Color.Black
    }

    override fun getGames(username: String): List<String> {
        return graph.getGames(username)
    }

    override fun getOtherUser(gameId: String, username: String): String {
        return graph.getOtherPlayer(gameId, username).first()
    }
}