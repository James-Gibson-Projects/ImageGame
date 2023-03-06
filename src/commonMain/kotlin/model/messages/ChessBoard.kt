package model.messages

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic
import kotlin.math.abs


@Serializable
data class ChessBoard(
    val id: String,
    val turn: Color,
    val spaces: List<List<Piece?>>,
    val whiteCastle: Boolean,
    val blackCastle: Boolean
) {
    operator fun get(position: Vec2): Piece? {
        return spaces[position.x][position.y]
    }
    operator fun contains(position: Vec2): Boolean {
        return position.x in 0..7 && position.y in 0..7
    }
    fun <T>map(transform: (Vec2, Piece?) -> T): List<List<T>> {
        return spaces.mapIndexed { x, col ->
            col.mapIndexed { y, piece ->
                transform(Vec2(x, y), piece)
            }
        }
    }
    fun getAllMoves(color: Color): List<PieceMoves>{
        return if(color != turn) listOf()
        else map { pos, piece ->
            if(piece == null || piece.color != color) null
            else PieceMoves(pos, piece.getMoves(pos, this))
        }.flatten().filterNotNull()
    }
    fun getAllValidMoves(color: Color): List<PieceMoves>{
        return getAllMoves(color).map {
            PieceMoves(it.piece, it.moves.filter { newPos ->
                !movePiece(it.piece, newPos).inCheck(color)
            })
        }
    }
    fun inCheck(color: Color): Boolean{
        var kingPos: Vec2? = null
        map { pos, piece->
            if (piece is Piece.King && piece.color == color){
                kingPos = pos
            }
        }
        return getAllMoves(color.opponentColor()).any { it.moves.contains(kingPos) }
    }
    fun movePiece(from: Vec2, to: Vec2) = ChessBoard(
        id,
        turn.opponentColor(),
        map { pos, piece ->
            when(pos){
                from -> null
                to -> get(from)
                else -> piece
            }
        },
        blackCastle,
        whiteCastle,
    )
    companion object {
        @JvmStatic
        fun startBoard() = ChessBoard(
            id = List(20){ ('A' .. 'Z').random() }.joinToString(""),
            turn = Color.White,
            spaces = listOf(
                listOf(Piece.Rook(Color.Black), Piece.Knight(Color.Black), Piece.Bishop(Color.Black), Piece.Queen(Color.Black), Piece.King(Color.Black), Piece.Bishop(Color.Black), Piece.Knight(Color.Black), Piece.Rook(Color.Black)),
                List(8){ Piece.Pawn(Color.Black) },
                List(8){ null },
                List(8){ null },
                List(8){ null },
                List(8){ null },
                List(8){ Piece.Pawn(Color.White) },
                listOf(Piece.Rook(Color.White), Piece.Knight(Color.White), Piece.Bishop(Color.White), Piece.Queen(Color.White), Piece.King(Color.White), Piece.Bishop(Color.White), Piece.Knight(Color.White), Piece.Rook(Color.White)),
            ),
            whiteCastle = false,
            blackCastle = false
        )

        @JvmStatic
        fun encode(board: ChessBoard) = board
            .map { _, piece -> Piece.encode(piece).toLong() }
            .flatten()

        @JvmStatic
        fun decode(spaces: List<Int>) = spaces.map { Piece.parse(it) }.chunked(8)
    }
}

@Serializable
data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun unaryMinus() = Vec2(-x, -y)
    operator fun minus(other: Vec2) = this + (-other)
    fun rotate90() = Vec2( -y, x)
}


@Serializable
sealed class Piece {
    abstract val color: Color
    fun Piece.isFriendly() = this@Piece.color == this.color
    fun Piece.isEnemy() = this@Piece.color != this.color
    abstract fun getMoves(position: Vec2, board: ChessBoard): List<Vec2>


    @Serializable
    class Pawn(override val color: Color): Piece(){
        override fun getMoves(position: Vec2, board: ChessBoard): List<Vec2> {
            val r = mutableListOf<Vec2>()
            val forwardOnce = position + color.forward
            if(forwardOnce in board && board[forwardOnce] == null){
                r.add(forwardOnce)
                if(
                    (color == Color.White && position.x == 6) ||
                    (color == Color.Black && position.x == 1)
                ){
                    val forwardTwice = forwardOnce + color.forward
                    if(forwardTwice in board && board[forwardTwice] == null)
                    r.add(forwardTwice)
                }
            }
            if(forwardOnce + left in board && board[forwardOnce + left]?.isEnemy() == true){
                r.add(forwardOnce + left)
            }
            if(forwardOnce + right in board && board[forwardOnce + right]?.isEnemy() == true){
                r.add(forwardOnce + right)
            }
            return r.toList()
        }
    }

    @Serializable
    sealed class StaticMoving: Piece(){
        protected abstract val moves: List<Vec2>
        override fun getMoves(position: Vec2, board: ChessBoard): List<Vec2> {
            return moves
                .map { it + position }
                .filter{ it in board && board[it]?.isFriendly() != true }
        }
    }

    @Serializable
    sealed class StraightMoving(): Piece(){
        protected abstract val direction: List<Vec2>
        override fun getMoves(position: Vec2, board: ChessBoard): List<Vec2> {
            return direction.flatMap {
                var toCheck = position + it
                val r = mutableListOf<Vec2>()
                while (toCheck in board){
                    val piece = board[toCheck]
                    when{
                        piece == null -> { r.add(toCheck); toCheck += it }
                        piece.isEnemy() -> { r.add(toCheck); break }
                        piece.isFriendly() -> break
                    }
                }
                r.toList()
            }
        }
    }

    @Serializable
    data class Knight(override val color: Color): StaticMoving() {
        override val moves: List<Vec2>
            get() = knightMoves
    }

    @Serializable
    data class Bishop(override val color: Color): StraightMoving() {
        override val direction: List<Vec2>
            get() = bishopDirections
    }

    @Serializable
    data class Rook(override val color: Color): StraightMoving() {
        override val direction: List<Vec2>
            get() = rookDirections
    }

    @Serializable
    data class Queen(override val color: Color): StraightMoving() {
        override val direction: List<Vec2>
            get() = bishopDirections + rookDirections
    }

    @Serializable
    data class King(override val color: Color): StaticMoving() {
        override val moves: List<Vec2>
            get() = bishopDirections + rookDirections
    }

    companion object{
        @JvmStatic
        fun parse(int: Int): Piece?{
            if(int == 0) return null
            val color = if(int < 0) Color.Black else Color.White
            return when(abs(int)){
                1 -> Pawn(color)
                2 -> Knight(color)
                3 -> Bishop(color)
                4 -> Rook(color)
                5 -> Queen(color)
                6 -> King(color)
                else -> throw Exception("Invalid piece id: $int")
            }
        }

        @JvmStatic
        fun encode(piece: Piece?): Int {
            if(piece == null) return 0
            val abs = when(piece){
                is Pawn   -> 1
                is Knight -> 2
                is Bishop -> 3
                is Rook   -> 4
                is Queen  -> 5
                is King   -> 6
            }
            return if(piece.color == Color.White) abs else -abs
        }
    }
}

@Serializable
enum class Color(val forward: Vec2){
    White(Vec2(-1, 0)),
    Black(Vec2(1, 0));
    fun opponentColor() = if(this == Black) White else Black
}

val left = Vec2(0, 1)
val right = Vec2(0, -1)

val bishopDirections = listOf(
    Vec2(1, 1),
    Vec2(1, -1),
    Vec2(-1, -1),
    Vec2(-1, 1),
)

val rookDirections = listOf(
    Vec2(1, 0),
    Vec2(0, 1),
    Vec2(-1, 0),
    Vec2(0, -1),
)
val knightMoves = rookDirections.flatMap {
    val forward = it + it
    val right = it.rotate90()
    listOf(forward + right, forward - right)
}