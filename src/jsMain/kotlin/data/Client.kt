package data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json


val defaultClient = HttpClient(Js){
    install(ContentNegotiation) {
        val converter = KotlinxSerializationConverter(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
        register(Json, converter)
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

class CustomResponseException(response: HttpResponse, s: String) : Exception(s)
