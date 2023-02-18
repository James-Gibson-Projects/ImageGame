package routes.plugins

import AuthenticationException
import AuthorizationException
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.UserNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<UserNotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<InviteAlreadySentException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }

    }
}