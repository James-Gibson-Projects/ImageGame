package api

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import model.UserCredentials
import net.malkowscy.application.routes.plugins.configureKoin
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import routes.http.configureUserRoutes
import routes.plugins.configureSecurity
import routes.plugins.configureSerialization

class UserLoginTest {
    @Test
    fun `User Sign-Up Golden Path`() = testApplication {
        application {
            configureSecurity()
            configureSerialization()
            configureKoin()
            configureUserRoutes()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        val response = client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        response.status `should be equal to` HttpStatusCode.OK
    }
}