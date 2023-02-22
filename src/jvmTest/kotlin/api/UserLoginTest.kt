package api

import data.db.Neo4jDatabase
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import model.UserCredentials
import routes.plugins.configureKoin
import org.amshove.kluent.`should be equal to`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import routes.http.configureUserRoutes
import routes.plugins.configureSecurity
import routes.plugins.configureSerialization
import uk.gibby.neo4k.core.Graph
import util.defaultModule

class UserLoginTest: KoinTest {

    private val graph: Graph
    init {
        startKoin{
            modules(defaultModule)
        }
        val db by inject<Neo4jDatabase>()
        graph = db.graph
        stopKoin()
    }

    @Before
    fun setup(){
        graph.delete()
    }

    @After
    fun endTest(){
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
    fun `User is authenticated after sign-up`() = testApplication {
        environment {
            developmentMode = false
        }
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
            addAuthRoute()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        val response = client.get("/auth-test")
        response.status `should be equal to` HttpStatusCode.OK
        response.bodyAsText() `should be equal to` "You are logged in"
    }

    @Test
    fun `User isn't authenticated after logout`() = testApplication {
        environment {
            developmentMode = false
        }
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
            addAuthRoute()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
        val response = client.get("/auth-test")
        response.status `should be equal to` HttpStatusCode.Unauthorized
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



    @Test
    fun `Login with incorrect username`() = testApplication {
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
        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
        val response = client.post("/api/login"){
            setBody(UserCredentials("NotAUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        response.status `should be equal to` HttpStatusCode.Unauthorized
    }
    @Test
    fun `Login with incorrect password`() = testApplication {
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

        val response = client.post("/api/login"){
            setBody(UserCredentials("TestUser", "BadPass123"))
            contentType(ContentType.Application.Json)
        }
        response.status `should be equal to` HttpStatusCode.Unauthorized
    }


    @Test
    fun `Login with valid credentials`() = testApplication {
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

        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
        val response = client.post("/api/login"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        response.status `should be equal to` HttpStatusCode.OK
    }


    @Test
    fun `Valid login authenticates the client`() = testApplication {
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
            addAuthRoute()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }
        client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
        client.post("/api/login"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }

        val response = client.get("/auth-test")
        response.status `should be equal to` HttpStatusCode.OK
        response.bodyAsText() `should be equal to` "You are logged in"
    }


    @Test
    fun `Invalid login doesn't authenticate the client`() = testApplication {
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
            addAuthRoute()
        }
        val client = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        client.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }
        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
        client.post("/api/login"){
            setBody(UserCredentials("TestUser", "BadPass123"))
            contentType(ContentType.Application.Json)
        }

        val response = client.get("/auth-test")
        response.status `should be equal to` HttpStatusCode.Unauthorized
    }

    @Test
    fun `Login from different client kicks other clients`() = testApplication {
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            configureUserRoutes()
            addAuthRoute()
        }
        val firstClient = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        val secondClient = createClient {
            install(ContentNegotiation) { json() }
            install(HttpCookies)
        }

        firstClient.post("/api/register"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }

        secondClient.post("/api/login"){
            setBody(UserCredentials("TestUser", "TestPass123"))
            contentType(ContentType.Application.Json)
        }

        val response = firstClient.get("/auth-test")
        response.status `should be equal to` HttpStatusCode.Unauthorized
    }
    private fun Application.addAuthRoute(){
        routing {
            authenticate("auth-session") {
                get("/auth-test"){
                    call.respond("You are logged in")
                }
            }
        }
    }
}