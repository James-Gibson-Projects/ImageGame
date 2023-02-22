import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import routes.plugins.configureKoin
import org.slf4j.event.Level
import routes.plugins.*
import util.Config
import util.config

fun main() {
    embeddedServer(
        Jetty,
        host = config[Config.serverHost],
        port = 8080,
        module = Application::applicationModule
    ).start(wait = true)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

fun Application.applicationModule() {
    install(Routing)
    configureKoin()
    configureSecurity()
    configureSerialization()
    configureWebsockets()
    configureStatusPages()
    configureRouting()
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
}

