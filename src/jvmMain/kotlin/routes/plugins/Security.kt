package routes.plugins

import domain.model.UserSession
import domain.repo.UserRepo
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val repo by inject<UserRepo>()
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 300
        }
    }
    install(Authentication) { session("auth-session") {
            validate { session ->
                repo.verifySession(session)
            }
            challenge {
                call.respondRedirect("/app/login")
            }
        }
    }

}
