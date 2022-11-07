package domain.models

data class Piece(val type: Type, val color: Color){
    fun getChar() = when(color){
        Color.White -> type.white
        Color.Black -> type.black
    }
    enum class Color{
        White,
        Black
    }
    enum class Type(val white: Char, val black: Char){
        Pawn('♙', '♟'),
        Bishop('♗', '♝'),
        Knight('♘','♞'),
        Rook('♖', '♜'),
        King('♔', '♚'),
        Queen('♕', '♛'),
    }
}