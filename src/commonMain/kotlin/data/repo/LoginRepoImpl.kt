package data.repo

import data.getClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginRepoImpl : LoginRepo, KoinComponent{
    val client: HttpClient = getClient()
    override suspend fun login(username: String, password: String) {
        client.post("/api/login"){
            setBody(UserCredentials(username, password))
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun register(username: String, password: String) {
        client.post("/api/register"){
            setBody(UserCredentials(username, password))
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun logout() {
        client.get("/api/logout")
    }
}