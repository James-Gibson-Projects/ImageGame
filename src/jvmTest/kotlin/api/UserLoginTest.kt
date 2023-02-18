package api

import data.db.Neo4jDatabase
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import routes.http.configureUserRoutes
import routes.plugins.configureSecurity
import routes.plugins.configureSerialization
import util.defaultModule

class UserLoginTest: KoinTest {


    @Before
    fun resetData(){
        startKoin{
            modules(defaultModule)
        }
        val db by inject<Neo4jDatabase>()
        db.graph.delete()
        stopKoin()
    }

    @Test
    fun `User sign-up golden path`() = testApplication {
        environment {
            developmentMode = false
        }
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
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

    @Test
    fun `Prevent duplicate user names`() = testApplication {
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }

        val response = client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        response.status `should be equal to` HttpStatusCode.Conflict
    }
}