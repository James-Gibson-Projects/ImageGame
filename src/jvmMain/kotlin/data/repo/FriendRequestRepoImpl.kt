package data.repo

import data.db.Neo4jDatabase
import data.db.schema.SentFriendRequestTo
import data.db.schema.UserNode
import domain.repo.FriendRequestRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.Graph
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

class FriendRequestRepoImpl : FriendRequestRepo, KoinComponent {

    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun sendRequest(fromUsername: String, toUsername: String) {
        graph.sendFriendRequest(fromUsername, toUsername)
    }

    override fun getUserIncomingInvites(name: String): List<String> {
        return graph.getInvites(name)
    }

    override fun getUserOutgoingInvites(name: String): List<String> {
        return graph.getSentInvites(name)
    }
    companion object{
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
    }
}