package routes.web_sockets

import domain.model.UserSession
import domain.repo.GameRepo
import io.ktor.server.websocket.*
import model.messages.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameRequestHandlerImpl : GameRequestHandler, KoinComponent {
    val repo: GameRepo by inject()
    override suspend fun shouldHandle(request: WebsocketRequest): Boolean {
        return request is GameRequest
    }

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) { }

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
        request as GameRequest
        when(request){
            is GameRequest.Refresh -> {
                val game = repo.getGameState(request.gameId)
                val color = repo.getColor(game.id, session.username)
                val moves = game.getAllValidMoves(color)
                sendSerialized<WebsocketResponse>(GameResponse(game, moves, color))
            }
            is GameRequest.MovePiece -> {
                val game = repo.getGameState(request.gameId)
                val color = repo.getColor(game.id, session.username)
                val moves = game.getAllValidMoves(color)
                if(request.to !in moves.first { it.piece == request.from }.moves) throw Exception("Invalid Move")
                val newState = game.movePiece(request.from, request.to)
                repo.setGameState(game.id, newState)
                sendSerialized<WebsocketResponse>(GameResponse(newState, newState.getAllValidMoves(color), color))
                connections[repo.getOtherUser(game.id, session.username)]!!
                    .webSocketSession
                    .sendSerialized<WebsocketResponse>(GameResponse(newState, newState.getAllValidMoves(color.opponentColor()), color.opponentColor()))
            }
        }
    }

    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) { }
}