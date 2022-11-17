package data.db.schema

import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates

@Node
@Relates(to = Space::class, by = "contains", data = EmptyData::class)
data class Game(val turn: Piece.Color)

@Node
@Relates(to = Space::class, by = "canMoveTo", data = CanMoveToData::class)
data class Space(val piece: Piece, val position: Vector2)

@RedisType
data class Vector2(val x: Long, val y: Long) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
}

@RedisType
data class CanMoveToData(val piece: Piece, val type: MoveType)

enum class MoveType{
    Passive,
    Targeting,
    Any
}

@RedisType
data class Piece(
    val type: Type,
    val color: Color
){
    enum class Type{
        Pawn,
        Rook,
        Knight,
        Bishop,
        Queen,
        King,
        Empty
    }
    enum class Color{
        White,
        Black,
        None
    }
}