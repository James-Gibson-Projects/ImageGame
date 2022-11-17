package routes.web_sockets

import domain.repo.FriendRequestRepo

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import data.db.schema.UserSession
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import net.malkowscy.application.domain.repo.InviteRepo
import model.InvitationsState
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureFriendRequestRoutes(){
    val repo by inject<FriendRequestRepo>()
    routing {
        authenticate("auth-session"){
            val connections = mutableMapOf<String, Connection>()
            webSocket("/api/friend_requests") {
                val userSession = call.principal<UserSession>()!!
                val thisConnection = Connection(this, userSession)
                connections += userSession.username to thisConnection
                try {
                    sendSerialized(repo.getUserIncomingInvites(userSession.username))
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val username = frame.readText()
                        val otherConnection = connections[username] ?: throw Exception("user connection not found")
                        try{ repo.sendRequest(userSession.username, username) }
                        catch (_: InviteAlreadySentException) {}
                        catch (_: SelfInviteException) {}
                        catch (_: UserNotFoundException) {}
                        otherConnection.webSocketSession.sendSerialized(repo.getUserIncomingInvites(otherConnection.userSession.username))
                    }
                }
                finally {
                    println("Removing $thisConnection!")
                    connections.remove(userSession.username)
                }
            }
        }
    }
}