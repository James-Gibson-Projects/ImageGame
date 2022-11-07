package presentation.views

import androidx.compose.runtime.Composable
import domain.models.ChessBoard
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun BoardView(){
    Board(ChessBoard())
}
@Composable
fun Board(state: ChessBoard){
    Table{
        state.spaces.forEachIndexed { x, row ->
            Tr {
                row.forEachIndexed { y, piece ->
                    Td(attrs = {
                        style {
                        width(64.px); height(64.px)
                        textAlign("center")
                        fontSize(48.px)
                        backgroundColor(if((x + y) % 2 == 0) Color.white else Color.lightgreen)
                    }}) {

                        piece?.let { Text("${it.getChar()}") }
                    }
                }
            }
        }
    }
}






