package data.db.schema

import model.messages.ChessBoard
import model.messages.Color
import uk.gibby.neo4k.returns.generic.ArrayReturn
import uk.gibby.neo4k.returns.graph.entities.Node
import uk.gibby.neo4k.returns.primitives.BooleanReturn
import uk.gibby.neo4k.returns.primitives.LongReturn
import uk.gibby.neo4k.returns.primitives.StringReturn
import uk.gibby.neo4k.returns.util.ReturnScope

class Game(
    val id: StringReturn,
    val board: ArrayReturn<Long, LongReturn>,
    val isWhitesTurn: BooleanReturn,
    val whiteHasCastled: BooleanReturn,
    val blackHasCastled: BooleanReturn,
): Node<ChessBoard>() {
    override fun ReturnScope.decode(): ChessBoard {
        val spaces = ChessBoard.decode(::board.result().map { it.toInt() })
        return ChessBoard(
            ::id.result(),
            if(::isWhitesTurn.result()) Color.White else Color.Black,
            spaces,
            ::whiteHasCastled.result(),
            ::blackHasCastled.result()
        )
    }
}