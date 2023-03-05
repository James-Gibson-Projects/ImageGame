package presentation.views

import androidx.compose.runtime.*
import model.messages.Color
import model.messages.Piece
import model.messages.Vec2
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import presentation.view_models.GameViewModel


@Composable
fun ChessBoardView(gameViewModel: GameViewModel){
    Div(attrs = { classes("grid", "grid-cols-8", "gap-0.5", "aspect-square") }) {
        val gameResponse by gameViewModel.piecesFlow.collectAsState(null)
        var selected: Vec2? by remember { mutableStateOf(null) }
        val targeting = gameResponse?.moves?.firstOrNull { it.piece == selected }?.moves
        if(gameResponse != null){
            gameResponse!!.state.spaces.forEachIndexed { x, row ->
                row.forEachIndexed { y, piece ->
                    Div(attrs = { onClick { selected = Vec2(x, y) }; classes(if((x+y)% 2 == 0) "bg-white" else "bg-green-500", "flex", "relative", "items-center", "justify-center", "aspect-square", "flex-row", "content-center", "justify-center") }){

                        if (piece != null) Img(src = piece.svg(), attrs = { classes("w-4/5", "h-4/5", "absolute") })
                        if(targeting?.contains(Vec2(x, y)) == true){
                            Div(attrs = { classes("rounded-full", "bg-gray-500", "bg-opacity-50", "w-1/2", "h-1/2", "absolute"); onClick { gameViewModel.movePiece(selected!!, Vec2(x, y)) } }) {

                            }
                        }
                    }
                }
            }
        }
    }
}

fun Piece.svg(): String{
    val typeText = when(this){
        is Piece.Pawn -> "Pawn"
        is Piece.Bishop -> "Bishop"
        is Piece.Knight-> "Knight"
        is Piece.Rook -> "Rook"
        is Piece.Queen-> "Queen"
        is Piece.King -> "King"
    }
    val colorText = when(color){
        Color.White -> "White"
        Color.Black -> "Black"
    }
    return "/static/svg/chess_pieces/$typeText$colorText.svg"
}