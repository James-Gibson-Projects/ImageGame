package data

import exceptions.CustomResponseException
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.json.*

actual fun getClient(): HttpClient = defaultClient

val defaultClient = HttpClient(CIO) {

    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingInterval = 20_000
    }
    HttpResponseValidator {
        validateResponse { response ->
            val code = response.status
            if (code != OK) {
                throw CustomResponseException(response, "Code: $code")
            }
        }
    }
}