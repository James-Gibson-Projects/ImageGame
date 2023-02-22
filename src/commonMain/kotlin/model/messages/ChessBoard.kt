package model.messages

import kotlinx.serialization.Serializable

data class ChessBoard(
    val turn: Piece.Color,
    val spaces: List<List<Piece>>,
    val whiteCastle: Boolean,
    val blackCastle: Boolean
)
data class Vec2(val x: Int, val y: Int) {
    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun unaryMinus() = Vec2(-x, -y)
    operator fun minus(other: Vec2) = this + (-other)
    fun rotate90() = Vec2( -y, x)
}

@Serializable
data class Piece(
    val type: Type,
    val color: Color
) {
    enum class Type {
        Pawn,
        Knight,
        Bishop,
        Rook,
        Queen,
        King
    }
    enum class Color {
        White,
        Black
    }
}


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

fun getMoves()