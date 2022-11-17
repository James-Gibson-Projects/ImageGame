package data.repo

import data.db.schema.UserSession
import domain.exceptions.UserNotFoundException
import domain.repo.FriendRepo
import domain.repo.FriendWebsocketRepo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import routes.web_sockets.Connection

class FriendWebsocketRepoImpl: FriendWebsocketRepo, KoinComponent {
    val repo by inject<FriendRepo>()
    private val connections = mutableMapOf<String, Connection>()

    override suspend fun updateUser(username: String) {
        (connections[username] ?: throw UserNotFoundException()).apply {
            webSocketSession.sendSerialized(repo.getFriends(userSession.username))
        }
    }

    override fun Application.configure() {
        routing {
            authenticate("auth-session"){
                webSocket("/api/friends") {
                    val userSession = call.principal<UserSession>()!!
                    val thisConnection = Connection(this, userSession)
                    connections += userSession.username to thisConnection
                    try { sendSerialized(repo.getFriends(userSession.username)) }
                    finally {
                        println("Removing $thisConnection!")
                        connections.remove(userSession.username)
                    }
                }
            }
        }
    }
}