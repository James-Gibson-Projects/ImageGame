package data.repo

import data.db.Neo4jDatabase
import domain.repo.GameRequestRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.core.Graph
import data.db.queries.*
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.NoIncomingInviteException
import kotlin.random.Random

class GameRequestRepoImpl : GameRequestRepo, KoinComponent{
    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }

    @Throws(InviteAlreadySentException::class)
    override fun sendRequest(fromUsername: String, toUsername: String) {
        if(toUsername in graph.getOutgoingRequests(fromUsername))
            throw InviteAlreadySentException()
        graph.createGameRequest(fromUsername, toUsername)
    }

    override fun getUserIncomingInvites(name: String): List<String> {
        return graph.getIncomingRequests(name)
    }

    override fun getUserOutgoingInvites(name: String): List<String> {
        return graph.getOutgoingRequests(name)
    }

    @Throws(NoIncomingInviteException::class)
    override fun acceptRequest(fromUsername: String, toUsername: String): String {
        val toUsersIncoming = graph.getIncomingRequests(toUsername)
        if(fromUsername !in toUsersIncoming)
            throw NoIncomingInviteException()
        graph.deleteGameRequest(fromUsername, toUsername)
        return if(Random.nextBoolean()){
            graph.createGame(fromUsername, toUsername, List(20){ ('A' .. 'Z').random() }.joinToString("")).first()
        } else {
            graph.createGame(toUsername, fromUsername, List(20){ ('A' .. 'Z').random() }.joinToString("")).first()
        }
    }
}