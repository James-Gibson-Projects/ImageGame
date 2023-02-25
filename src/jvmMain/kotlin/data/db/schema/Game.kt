package data.db.schema

import model.messages.ChessBoard
import model.messages.Color
import model.messages.Piece
import uk.gibby.neo4k.returns.generic.ArrayReturn
import uk.gibby.neo4k.returns.graph.entities.Node
import uk.gibby.neo4k.returns.primitives.BooleanReturn
import uk.gibby.neo4k.returns.primitives.LongReturn
import uk.gibby.neo4k.returns.primitives.StringReturn
import uk.gibby.neo4k.returns.util.ReturnScope

class Game(
    val board: ArrayReturn<List<Long>, ArrayReturn<Long, LongReturn>>,
    val isWhitesTurn: BooleanReturn,
    val whiteHasCastled: BooleanReturn,
    val blackHasCastled: BooleanReturn,
    val id: StringReturn
): Node<ChessBoard>() {
    override fun ReturnScope.decode(): ChessBoard {
        val spaces = ::board.result().map { col ->
            col.map { Piece.parse(it.toInt()) }
        }
        return ChessBoard(
            if(::isWhitesTurn.result()) Color.White else Color.Black,
            spaces,
            ::whiteHasCastled.result(),
            ::blackHasCastled.result()
        )
    }
}