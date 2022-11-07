import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.jetty.Jetty
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import kotlinx.html.*
import net.malkowscy.application.domain.exceptions.InviteAlreadySentException
import net.malkowscy.application.domain.exceptions.UserNotFoundException
import net.malkowscy.application.routes.plugins.configureKoin
import net.malkowscy.application.routes.plugins.configureRouting
import net.malkowscy.application.routes.plugins.configureSecurity
import net.malkowscy.application.routes.plugins.configureWebsockets
import routes.plugins.configureSerialization
import routes.plugins.configureTemplating

/*
fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/js.js") {}
    }
}

 */

fun main() {
    embeddedServer(Jetty, port = 8080, host = "127.0.0.1") {
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