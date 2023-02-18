package data.repo

import data.db.Neo4jDatabase
import domain.exceptions.InviteAlreadySentException
import domain.repo.FriendRequestRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.core.Graph
import data.db.queries.*
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException

class FriendRequestRepoImpl : FriendRequestRepo, KoinComponent {

    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun sendRequest(fromUsername: String, toUsername: String) {
        when{
            fromUsername == toUsername -> throw SelfInviteException()
            !graph.userExists(toUsername).first() -> throw UserNotFoundException()
            (toUsername in graph.getSentInvites(fromUsername)) -> throw InviteAlreadySentException()
        }
        if(toUsername in getUserIncomingInvites(fromUsername)) graph.createFriendship(toUsername, fromUsername)
        else graph.sendFriendRequest(fromUsername, toUsername)
    }

    override fun getUserIncomingInvites(name: String): List<String> {
        return graph.getInvites(name)
    }

    override fun getUserOutgoingInvites(name: String): List<String> {
        return graph.getSentInvites(name)
    }

    override fun acceptRequest(fromUsername: String, toUsername: String) {
        if (toUsername in graph.getSentInvites(fromUsername)){
            graph.createFriendship(fromUsername, toUsername)
        }
    }
}