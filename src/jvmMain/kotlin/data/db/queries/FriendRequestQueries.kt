package data.db.queries

import data.db.schema.FriendsWith
import data.db.schema.SentFriendRequestTo
import data.db.schema.UserNode
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Delete.Companion.delete
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.paths.`-o-`
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

val sendFriendRequest = query(::StringReturn, ::StringReturn){ from, to ->
    val (fromUser, toUser) = match(::UserNode{ it[username] = from }, ::UserNode{ it[username] = to })
    create(fromUser `o-→` ::SentFriendRequestTo `o-→` toUser)
}.build()

val getInvites = query(::StringReturn){ name ->
    val (other) = match(::UserNode `o-→` ::SentFriendRequestTo `o-→` ::UserNode{ it[username] = name})
    other.username
}.build()

val getSentInvites = query(::StringReturn){ name ->
    val (_, _, other) = match(::UserNode{ it[username] = name} `o-→` ::SentFriendRequestTo `o-→` ::UserNode)
    other.username
}.build()

val createFriendship = query(::StringReturn, ::StringReturn){ from, to ->
    val (fromUser, request, toUser) = match(::UserNode{ it[username] = from } `o-→` ::SentFriendRequestTo `o-→` ::UserNode{ it[username] = to })
    delete(request)
    create(fromUser `-o-` ::FriendsWith `-o-` toUser)
}.build()
