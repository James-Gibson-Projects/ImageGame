package data.repo

import domain.exceptions.UserNotFoundException
import domain.model.UserSession
import domain.repo.FriendRepo
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import routes.web_sockets.Connection