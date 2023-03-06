package presentation.view_models

import data.repo.GameRequestClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.messages.Vec2
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameViewModel(private val gameId: String, val scope: CoroutineScope): KoinComponent{
    private val repo: GameRequestClientRepo by inject()
    val piecesFlow = repo.observeGameState()
    fun refresh(){
        scope.launch {
            repo.refresh(gameId)
        }
    }
    fun movePiece(from: Vec2, to: Vec2){
        scope.launch {
            repo.movePiece(gameId, from, to)
        }
    }
}