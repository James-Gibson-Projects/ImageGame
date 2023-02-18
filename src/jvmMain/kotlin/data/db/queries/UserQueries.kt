package data.db.queries

import data.db.schema.*
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.DetachDelete.Companion.detachDelete
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.functions.exists
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.paths.`←-o`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

val createUser = query(::StringReturn, ::StringReturn) { name, passHash ->
    create(::UserNode{ it[username] = name; it[passwordHash] = passHash })
}.build()

val userExists = query(::StringReturn) { name ->
    exists(::UserNode{ it[username] = name })
}.build()

val findUser = query(::StringReturn) { name ->
    match(::UserNode{ it[username] = name})
}.build()

val createSession = query(::StringReturn, ::StringReturn) { name, sessionId->
    val user = match(::UserNode{ it[username] = name})
    val (_, _, session) = create(user `o-→` ::AuthenticatedBy `o-→` ::UserSessionNode{ it[key] = sessionId; it[username] = user.username })
    session
}.build()

val logoutUser = query(::StringReturn){ name ->
    val (session) = match(::UserSessionNode `←-o` ::AuthenticatedBy `←-o` ::UserNode{ it[username] = name })
    detachDelete(session)
}.build()

val findSession = query (::StringReturn, ::StringReturn){ name, key ->
    match(::UserSessionNode{it[username] = name; it[this.key] = key})
}.build()
