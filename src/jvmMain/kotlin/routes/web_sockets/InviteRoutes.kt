package routes.web_sockets

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import domain.model.UserSession
import domain.repo.InviteRepo
import model.messages.InvitationsState
import org.koin.ktor.ext.inject
import java.util.*



fun Application.configureInviteRoutes(){
    val repo by inject<InviteRepo>()
    routing {
        authenticate("auth-session"){
            val connections = mutableMapOf<String, Connection>()
            webSocket("/api/invites") {
                println("Adding user!")
                val userSession = call.principal<UserSession>()!!
                val thisConnection = Connection(this, userSession)
                connections += userSession.username to thisConnection
                try {
                    sendSerialized(
                        InvitationsState(
                            repo.getUserOutgoingInvites(userSession.username),
                            repo.getUserIncomingInvites(userSession.username)
                        )
                    )
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val username = frame.readText()
                        val otherConnection = connections[username] ?: throw Exception("user connection not found")
                        try{ repo.sendInvite(userSession.username, username) }
                            catch (_: InviteAlreadySentException) {}
                            catch (_: SelfInviteException) {}
                            catch (_: UserNotFoundException) {}
                        sendSerialized(
                            InvitationsState(
                                repo.getUserOutgoingInvites(userSession.username),
                                repo.getUserIncomingInvites(userSession.username)
                            )
                        )
                        otherConnection.webSocketSession.sendSerialized(
                            InvitationsState(
                                repo.getUserOutgoingInvites(otherConnection.userSession.username),
                                repo.getUserIncomingInvites(otherConnection.userSession.username)
                            )
                        )

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
data class Connection(val webSocketSession: DefaultWebSocketServerSession, val userSession: UserSession)