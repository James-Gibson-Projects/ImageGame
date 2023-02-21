import exceptions.CustomResponseException
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val defaultClient = HttpClient(Js){
    install(HttpCookies)
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingInterval = 20_000
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    HttpResponseValidator {
        validateResponse {
            if(it.status != OK){
                throw Exception(it.bodyAsText())
            }
        }
    }
}