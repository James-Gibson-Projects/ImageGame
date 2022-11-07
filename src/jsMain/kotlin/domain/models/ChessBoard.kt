package domain.models

class ChessBoard(val spaces: List<List<Piece?>>){
    constructor(): this(
        (0..7).map { x ->
            List(8){ y ->
                when{
                    x == 1 ->  Piece(Piece.Type.Pawn, Piece.Color.Black)
                    x == 6 ->  Piece(Piece.Type.Pawn, Piece.Color.White)
                    else -> null
                }
            }
        }
    )
}