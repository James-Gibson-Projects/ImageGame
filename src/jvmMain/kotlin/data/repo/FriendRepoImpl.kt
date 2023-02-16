package data.repo

import data.db.Neo4jDatabase
import data.db.schema.*
import domain.model.UserWithStatus
import domain.repo.FriendRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.clauses.Delete.Companion.delete
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.Graph
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.functions.exists
import uk.gibby.neo4k.paths.`-o-`
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.multiple.many
import uk.gibby.neo4k.returns.primitives.StringReturn

class FriendRepoImpl : FriendRepo, KoinComponent{

    val graph: Graph
    init{
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun getFriends(username: String): List<UserWithStatus> {
        return graph.findFriendsWithStatus(username)
            .map { UserWithStatus(it.first, it.second) }
    }

    override fun deleteFriendship(firstUsername: String, secondUsername: String) {
        graph.deleteFriendship(firstUsername, secondUsername)
    }
    companion object{
        val findFriendsWithStatus = query(::StringReturn) { name ->
            val (friend) = match(::UserNode `-o-` ::FriendsWith `-o-` ::UserNode{ it[username] = name })
            many(friend.username, exists(friend `o-→` ::AuthenticatedBy `o-→` ::UserSessionNode))
        }.build()

        val deleteFriendship = query(::StringReturn, ::StringReturn) { firstName, secondName->
            val (_, friendship) = match(::UserNode{ it[username] = firstName } `-o-` ::FriendsWith `-o-` ::UserNode{ it[username] = secondName })
            delete(friendship)
        }.build()
    }
}