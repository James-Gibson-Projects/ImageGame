import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import domain.exceptions.InviteAlreadySentException
import domain.exceptions.UserNotFoundException
import net.malkowscy.application.routes.plugins.configureKoin
import net.malkowscy.application.routes.plugins.configureRouting
import routes.plugins.configureSecurity
import routes.plugins.configureWebsockets
import routes.plugins.configureSerialization
import routes.plugins.configureTemplating
import util.Config
import util.config

fun main() {
    embeddedServer(Jetty, port = 8080, host = config[Config.serverHost]) {
        install(Routing)
        configureKoin()
        configureSecurity()
        configureTemplating()
        configureSerialization()
        configureWebsockets()
        install(StatusPages) {
            exception<AuthenticationException> { call, _ ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { call, _ ->
                call.respond(HttpStatusCode.Forbidden)
            }
            exception<UserNotFoundException>{ call, _ ->
                call.respond(HttpStatusCode.NotFound)
            }
            exception<InviteAlreadySentException>{ call, _ ->
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        configureRouting()
    }.start(wait = true)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()