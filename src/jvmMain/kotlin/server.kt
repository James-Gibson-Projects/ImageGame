import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import io.ktor.server.routing.*

import net.malkowscy.application.routes.plugins.configureKoin
import routes.plugins.configureRouting
import routes.plugins.*
import util.Config
import util.config

fun main() {
    embeddedServer(
        Jetty,
        port = 8080,
        host = config[Config.serverHost],
        module = Application::applicationModule
    ).start(wait = true)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

fun Application.applicationModule() {
    install(Routing)
    configureKoin()
    configureSecurity()
    configureTemplating()
    configureSerialization()
    configureWebsockets()
    configureStatusPages()
    configureRouting()
}

