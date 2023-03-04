package presentation.view_models

import data.websocket.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.messages.ChessBoard
import model.messages.GameRequest
import model.messages.GameResponse
import model.messages.Vec2
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameViewModel(private val gameId: String): KoinComponent{
    private val ws: WebSocket by inject()
    val gameFlow = ws.observeResponses(GameResponse::class, GameRequest.Refresh(gameId))
    private val scope = CoroutineScope(Dispatchers.Default)
    fun movePiece(from: Vec2, to: Vec2){
        scope.launch {
            ws.sendRequest(GameRequest.MovePiece(gameId, from, to))
        }
    }
}