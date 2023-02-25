package data.db.queries

import data.db.schema.SentGameRequestTo
import data.db.schema.UserNode
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Delete.Companion.delete
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

val createGameRequest = query(::StringReturn, ::StringReturn){ fromUsername, toUsername ->
    val (from, to) = match(::UserNode{ it[username] = fromUsername }, ::UserNode{ it[username] = toUsername })
    create(from `o-→` ::SentGameRequestTo `o-→` to)
}

val deleteGameRequest = query(::StringReturn, ::StringReturn) { fromUsername, toUsername ->
    val (_, sentFriendRequest, _) = match(::UserNode{ it[username] = fromUsername } `o-→` ::SentGameRequestTo `o-→`  ::UserNode{ it[username] = toUsername })
    delete(sentFriendRequest)
}

