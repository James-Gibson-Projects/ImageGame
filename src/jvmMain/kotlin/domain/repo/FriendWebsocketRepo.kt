package domain.repo

import io.ktor.server.application.*

interface FriendWebsocketRepo {
    suspend fun updateUser(username: String)
    fun Application.configure()
}