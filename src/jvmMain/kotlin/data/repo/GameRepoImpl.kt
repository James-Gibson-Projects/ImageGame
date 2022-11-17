package net.malkowscy.application.data.repo

import uk.gibby.redis.core.invoke
import data.db.Database
import data.db.schema.MoveType
import data.db.schema.Piece
import data.db.schema.Vector2
import net.malkowscy.application.domain.repo.GameRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.redis.conditions.array.contains
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.toValue
import uk.gibby.redis.functions.math.id
import uk.gibby.redis.functions.math.plus
import uk.gibby.redis.functions.switch
import uk.gibby.redis.generated.*
import uk.gibby.redis.paths.minus
import uk.gibby.redis.results.ArrayResult
import uk.gibby.redis.results.BooleanResult
import uk.gibby.redis.results.array
import uk.gibby.redis.results.of
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.Update.Companion.set
import uk.gibby.redis.statements.Where.Companion.where
import uk.gibby.redis.statements.WithAs.Companion.using

class GameRepoImpl: GameRepo, KoinComponent {
    val db by inject<Database>()
    override fun createGame(): Long {
        return  db.graph.query {
            val game = create(::GameNode)
            val boardData = array(array(::PieceResult)) of initialBoard
            val row = unwind(boardData)
            val spaceData = unwind(row as ArrayResult<*, *>)
            create(game - {contains} - ::SpaceNode{ it[piece] = spaceData as PieceResult })
            id(game)
        }.first()
    }

    override fun addUserToGame(name: String, gameId: Long, userColor: Piece.Color) {
        db.graph.query {
            val (user, game) = match(::UserNode{it[username] = name}, ::GameNode)
            where(id(game) eq gameId)
            create(user - { inGame{ it[color] = userColor } } - game)
        }
    }

    override fun QueryScope.addBoardPiece(gameId: Long, position: Vector2, piece: Piece): EmptyResult {
        val game = match(::GameNode)
        where(id(game) eq gameId)
        val gameRef = using(game)
        TODO()
        when(piece.type){

        }
        TODO()
    }
    private fun QueryScope.addPawn(game: GameNode, at: Vector2, pieceData: Piece){
        val pieceSpace = match(game - {contains} - ::SpaceNode{it[position] = at}).second
        set(pieceSpace.piece toValue  (PieceAttribute() of pieceData))
        val forward = when(pieceData.color){
            Piece.Color.White -> Vector2(0, 1)
            Piece.Color.Black -> Vector2(0, -1)
            else -> throw Exception("Given empty color")
        }
        val attackingPositions = listOf(forward + Vector2(1, 0), forward + Vector2(1, 0)).map { it + at }
        val passivePos = at + forward
        val passiveSpace = match(game -{contains} - ::SpaceNode{it[position] = passivePos}).second
        create(pieceSpace - { canMoveTo { it[type] = MoveType.Passive; it[piece] = pieceData } } - passiveSpace).second
        val attackingSpace = match(game - {contains} - ::SpaceNode).second
        where(array(::Vector2Result) of attackingPositions contains attackingSpace.position)
        create(pieceSpace - { canMoveTo { it[type] = MoveType.Targeting; it[piece] = pieceData } } - attackingSpace)
    }
    private fun QueryScope.addDirection(direction: Vector2, game: GameNode, at: Vector2, pieceData: Piece){
        val pieceSpace = match(game - {contains} - ::SpaceNode{it[position] = at}).second
        set(pieceSpace.piece toValue  (PieceAttribute() of pieceData))
        val from = using(match(game - {contains} - ::SpaceNode).second)
        val to = using(match(game - {contains} - ::SpaceNode)).second
        where(((from.position + direction) eq to.position) and  TODO() )

    }
    private fun Vector2Result.inViewOf(at: Vector2): BooleanResult{
        switch(x, BooleanResult::class){
            0L then switch(y){
                0L then true
                TODO()
            }
        }
        return TODO()
    }
    operator fun Vector2Result.plus(other: Vector2Result) = object: Vector2Result(){
        override val x = this@plus.x + other.x
        override val y = this@plus.y + other.y
    }
    operator fun Vector2Result.plus(other: Vector2) = this + (Vector2Result() of other)
    companion object{
        @JvmStatic
        val initialBoard = listOf(
            listOf(Piece.Type.Rook, Piece.Type.Knight, Piece.Type.Bishop, Piece.Type.Queen, Piece.Type.King, Piece.Type.Bishop, Piece.Type.Knight, Piece.Type.Rook),
            List(8){ Piece.Type.Pawn},
            List(8){ Piece.Type.Empty},List(8){ Piece.Type.Empty},List(8){ Piece.Type.Empty},List(8){ Piece.Type.Empty},
            List(8){ Piece.Type.Pawn},
            listOf(Piece.Type.Rook, Piece.Type.Knight, Piece.Type.Bishop, Piece.Type.King, Piece.Type.Queen, Piece.Type.Bishop, Piece.Type.Knight, Piece.Type.Rook)
        ).mapIndexed { index, rows ->
            if(index < 4) rows.map { Piece(it, Piece.Color.White) }
            else rows.map { Piece(it, Piece.Color.Black) }
        }
    }
}