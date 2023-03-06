package data.db.queries

import data.db.schema.Game
import data.db.schema.PlayingIn
import data.db.schema.UserNode
import model.messages.ChessBoard
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.clauses.Set.Companion.set
import uk.gibby.neo4k.core.array
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.core.of
import uk.gibby.neo4k.functions.boolean_return.not
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.paths.`←-o`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.LongReturn
import uk.gibby.neo4k.returns.primitives.StringReturn

val createGame = query(::StringReturn, ::StringReturn, ::StringReturn) { whiteName, blackName, gameId ->
    val newBoard = ChessBoard.startBoard()
    val spaces = array(::LongReturn) of ChessBoard.encode(newBoard)
    val (whitePlayer, blackPlayer) =
        match(::UserNode{ it[username] = whiteName }, ::UserNode{ it[username] = blackName})
    val (_, _, game) = create(whitePlayer `o-→` ::PlayingIn { it[playingAsWhite] = true } `o-→` ::Game {
        it[board] = spaces
        it[isWhitesTurn] = true
        it[whiteHasCastled] = false
        it[blackHasCastled] = false
        it[this.id] = gameId
    } `←-o` ::PlayingIn { it[playingAsWhite] = false } `←-o` blackPlayer)
    game.id
}.build()

val getGame = query(::StringReturn) { gameId ->
    match(::Game { it[id] = gameId })
}.build()

val setGame = query(::StringReturn, array(::LongReturn)) { gameId, board ->
    val game = match(::Game { it[id] = gameId })
    set { game.board to board; game.isWhitesTurn to !game.isWhitesTurn}
}.build()

val getGames = query(::StringReturn) { username ->
    val (game) = match(::Game `←-o` ::PlayingIn `←-o` ::UserNode{ it[this.username] = username })
    game.id
}.build()

val getIsPlayingAsWhite = query(::StringReturn, ::StringReturn) { gameId, username ->
    val (_, inGame) = match(::Game{ it[id] = gameId } `←-o` ::PlayingIn `←-o` ::UserNode{ it[this.username] = username })
    inGame.playingAsWhite
}.build()

val getOtherPlayer = query(::StringReturn, ::StringReturn) { gameId, username ->
    val (other) = match(::UserNode `o-→` ::PlayingIn `o-→` ::Game { it[id] = gameId } `←-o` ::PlayingIn `←-o` ::UserNode{ it[this.username] = username })
    other.username
}.build()
