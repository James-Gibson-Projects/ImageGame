package routes.web_sockets

import domain.model.UserSession
import domain.repo.UserRepo
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import model.messages.WebsocketRequest
import org.eclipse.jetty.util.log.Log
import org.koin.ktor.ext.inject

val connections = mutableMapOf<String, Connection>()
fun Application.configureWebsocket(){
    val friendRequests: FriendRequestHandler by inject()
    val gameInviteRequests: GameInviteRequestHandler by inject()
    val gameRequests: GameRequestHandler by inject()
    val userRepo: UserRepo by inject()
    val handlers = listOf(friendRequests, gameRequests, gameInviteRequests)
    routing {
        authenticate("auth-session"){
            webSocket("/live") {
                val userSession = call.principal<UserSession>()!!
                val thisConnection = Connection(this, userSession)
                connections += userSession.username to thisConnection
                try {
                    handlers.forEach { with(it){ onConnect(userSession) } }
                    for (frame in incoming) {
                        val request = converter!!.deserialize<WebsocketRequest>(frame)
                        handlers
                            .first { it.shouldHandle(request) }
                            .apply { handle(userSession, request) }
                    }
                }
                finally {
                    handlers.forEach { it.apply { onClose(userSession) } }
                    connections.remove(userSession.username)
                    userRepo.logoutUser(userSession)
                }
            }
        }
    }
}
