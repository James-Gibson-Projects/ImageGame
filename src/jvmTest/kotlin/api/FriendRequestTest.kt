package api

import applicationModule
import data.db.Neo4jDatabase
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import model.messages.InvitationsState
import model.messages.InviteResponse
import model.UserCredentials
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import uk.gibby.neo4k.core.Graph
import util.defaultModule

class FriendRequestTest: KoinTest{

    private val graph: Graph
    init {
        startKoin{ modules(defaultModule) }
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
    fun `Can't send friend request with invalid username`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val client = createUser(firstUserDetails)
        client.webSocket("/api/friend_requests"){
            receiveDeserialized<InviteResponse>()
            sendSerialized("Not_A_User")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Error::class.java
            (response as InviteResponse.Error).message `should be equal to` "User not found: Not_A_User"
        }
    }

    @Test
    fun `A user can't send a friend request to themself`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val client = createUser(firstUserDetails)
        client.webSocket("/api/friend_requests"){
            receiveDeserialized<InviteResponse>()
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Error::class.java
            (response as InviteResponse.Error).message `should be equal to` "You can't send a friend request to yourself"
        }
    }

    @Test
    fun `Can send friend request to valid username`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val secondUserDetails = UserCredentials("TestUser2", "TestPass123")
        createUserAndLogout(firstUserDetails)
        val client = createUser(secondUserDetails)
        client.webSocket("/api/friend_requests"){
            receiveDeserialized<InviteResponse>()
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
            (response as InviteResponse.Success).state.outgoing `should contain` "TestUser1"
        }
    }


    @Test
    fun `Can't send friend request to the same user more than once`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val secondUserDetails = UserCredentials("TestUser2", "TestPass123")
        createUserAndLogout(firstUserDetails)
        val client = createUser(secondUserDetails)
        client.webSocket("/api/friend_requests"){
            receiveDeserialized<InviteResponse>()
            sendSerialized("TestUser1")
            receiveDeserialized<InviteResponse>()
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Error::class.java
            (response as InviteResponse.Error).message `should be equal to` "Friend request already sent"
        }
    }

    @Test
    fun `Friend requests are sent on connect`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val secondUserDetails = UserCredentials("TestUser2", "TestPass123")
        val firstClient = createUser(firstUserDetails)
        val secondClient = createUser(secondUserDetails)
        secondClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state `should be equal to` InvitationsState(listOf(), listOf())
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
        }

        firstClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state.incoming `should contain` "TestUser2"
        }
    }

    @Test
    fun `Friend request is deleted when it is accepted`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val secondUserDetails = UserCredentials("TestUser2", "TestPass123")
        val firstClient = createUser(firstUserDetails)
        val secondClient = createUser(secondUserDetails)
        secondClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state `should be equal to` InvitationsState(listOf(), listOf())
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
        }
        firstClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state.incoming `should contain` "TestUser2"
            sendSerialized("TestUser2")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
            (response as InviteResponse.Success).state.incoming.size `should be equal to` 0
        }
    }

    @Test
    fun `Users become friends after accepting a friend request`() = testApplication {
        application(Application::applicationModule)
        val firstUserDetails = UserCredentials("TestUser1", "TestPass123")
        val secondUserDetails = UserCredentials("TestUser2", "TestPass123")
        val firstClient = createUser(firstUserDetails)
        val secondClient = createUser(secondUserDetails)
        secondClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state `should be equal to` InvitationsState(listOf(), listOf())
            sendSerialized("TestUser1")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
        }
        firstClient.webSocket("/api/friend_requests"){
            val initial = receiveDeserialized<InviteResponse>()
            initial `should be instance of` InviteResponse.Success::class.java
            (initial as InviteResponse.Success).state.incoming `should contain` "TestUser2"
            sendSerialized("TestUser2")
            val response = receiveDeserialized<InviteResponse>()
            response `should be instance of` InviteResponse.Success::class.java
            (response as InviteResponse.Success).state.incoming.size `should be equal to` 0
        }
    }
    private suspend fun ApplicationTestBuilder.createUserAndLogout(details: UserCredentials){
        val client = defaultClient()
        client.post("/api/register"){
            setBody(details)
            contentType(ContentType.Application.Json)
        }.status `should be equal to` HttpStatusCode.OK
        client.get("/api/logout").status `should be equal to` HttpStatusCode.OK
    }

    private suspend fun ApplicationTestBuilder.createUser(details: UserCredentials): HttpClient {
        val client = defaultClient()
        client.post("/api/register"){
            setBody(details)
            contentType(ContentType.Application.Json)
        }.status `should be equal to` HttpStatusCode.OK
        return client
    }

    private fun ApplicationTestBuilder.defaultClient() = createClient {
        install(ContentNegotiation) { json() }
        install(HttpCookies)
        install(WebSockets){
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
}