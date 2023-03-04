package data.db.queries

import data.db.schema.Game
import data.db.schema.PlayingIn
import data.db.schema.UserNode
import model.messages.ChessBoard
import model.messages.Piece
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.clauses.Set.Companion.set
import uk.gibby.neo4k.core.array
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.core.of
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.paths.`←-o`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.LongReturn
import uk.gibby.neo4k.returns.primitives.StringReturn

val createGame = query(::StringReturn, ::StringReturn) { whiteName, blackName ->
    val spaces = array(array(::LongReturn)) of ChessBoard.startBoard
        .map { _, piece -> Piece.encode(piece).toLong() }
    val (whitePlayer, blackPlayer) =
        match(::UserNode{ it[username] = whiteName }, ::UserNode{ it[username] = blackName})
    val (_, _, game) = create(whitePlayer `o-→` ::PlayingIn { it[playingAsWhite] = true } `o-→` ::Game {
        it[board] = spaces
        it[isWhitesTurn] = true
        it[whiteHasCastled] = false
        it[blackHasCastled] = false
        it[this.id] = id
    } `←-o` ::PlayingIn { it[playingAsWhite] = false } `←-o` blackPlayer)
    game.id
}.build()

val getGame = query(::StringReturn) { gameId ->
    match(::Game { it[id] = gameId })
}.build()

val setGame = query(::StringReturn, array(array(::LongReturn))) { gameId, board ->
    val game = match(::Game { it[id] = gameId })
    set { game.board to board }
}.build()

val getGames = query(::StringReturn) { username ->
    val (game) = match(::Game `←-o` ::PlayingIn `←-o` ::UserNode{ it[this.username] = username })
    game.id
}.build()
