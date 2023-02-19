package data.repo

import data.remote.Remote
import domain.repos.UserRepo
import io.ktor.client.request.*
import io.ktor.http.*
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepoImpl: UserRepo, KoinComponent {
    private val remote by inject<Remote>()
    override suspend fun login(details: UserCredentials){
        remote.client.post("/api/login") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
    }
    override suspend fun register(details: UserCredentials){
        remote.client.post("/api/register") {
            contentType(ContentType.Application.Json)
            setBody(details)
        }
    }
}