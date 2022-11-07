package data.repo

import data.database.Remote
import domain.repos.UserRepo
import io.ktor.client.request.*
import io.ktor.http.*
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepoImpl: UserRepo, KoinComponent {
    private val remote by inject<Remote>()
    override suspend fun login(details: UserCredentials): Boolean {
        val response = remote.client.post("/api/login") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
        return response.status == HttpStatusCode.OK
    }
    override suspend fun register(details: UserCredentials): Boolean {
        val response = remote.client.post("/api/register") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
        return response.status == HttpStatusCode.OK
    }
}