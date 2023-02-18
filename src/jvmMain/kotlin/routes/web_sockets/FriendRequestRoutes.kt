package routes.web_sockets

import domain.repo.FriendRequestRepo

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import domain.model.UserSession
import io.ktor.serialization.*
import model.messages.InvitationsState
import model.messages.InviteResponse
import org.koin.ktor.ext.inject

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
                    sendSerialized<InviteResponse>(InviteResponse.Success(InvitationsState(repo.getUserOutgoingInvites(userSession.username), repo.getUserIncomingInvites(userSession.username))))
                    for (frame in incoming) {
                        val username = converter!!.deserialize<String>(frame)
                        try {
                            repo.sendRequest(userSession.username, username)
                            sendSerialized<InviteResponse>(InviteResponse.Success(InvitationsState(repo.getUserOutgoingInvites(userSession.username), repo.getUserIncomingInvites(userSession.username))))
                        }
                        catch (_: InviteAlreadySentException) {
                            sendSerialized<InviteResponse>(InviteResponse.Error("Friend request already sent"))
                        }
                        catch (_: UserNotFoundException) {
                            sendSerialized<InviteResponse>(InviteResponse.Error("User not found: $username"))
                        }
                        catch (_: SelfInviteException) {
                            sendSerialized<InviteResponse>(InviteResponse.Error("You can't send a friend request to yourself"))
                        }
                        val otherConnection = connections[username] ?: continue
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