package routes.plugins

import domain.model.UserSession
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import domain.repo.FriendWebsocketRepo
import org.koin.ktor.ext.inject
import routes.web_sockets.Connection
import routes.web_sockets.configureFriendRequestRoutes
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet


fun Application.configureWebsockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    val friendWebsocketRepo: FriendWebsocketRepo by inject()
    with(friendWebsocketRepo){
        configure()
    }
    configureFriendRequestRoutes()
    routing {
        authenticate("auth-session"){
            val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
            webSocket("/chat") {
                println("Adding user!")
                val userSession = call.principal<UserSession>()!!
                val thisConnection = Connection(this, userSession)
                connections += thisConnection
                try {
                    send("You are connected! There are ${connections.count()} users here.")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val textWithUsername = "[${thisConnection.userSession.username}]: $receivedText"
                        connections.forEach {
                            it.webSocketSession.send(textWithUsername)
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing $thisConnection!")
                    connections -= thisConnection
                }
            }
        }
    }
}
class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}