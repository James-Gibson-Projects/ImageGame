package data.repo

import data.database.Remote
import domain.repos.FriendsRepo
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.ActiveStatus
import model.InvitationsState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendsRepoImpl: FriendsRepo, KoinComponent{
    private val remote by inject<Remote>()
    private var session: DefaultClientWebSocketSession? = null
    private val launchJob = MainScope().async {
        session = remote.client.webSocketSession(
            method = HttpMethod.Get,
            host = "192.168.1.118",
            port = 8080,
            path = "/api/friends"
        )
    }
    override fun observeFriends(): Flow<List<Pair<String, Long>>> {
        return flow {
            emit(emptyList())
            launchJob.await()
            try{
                emitAll(session!!.incoming.receiveAsFlow()
                    .map { it as Frame.Text }
                    .map { Json.decodeFromString(it.readText()) }
                )
            } catch (e: Exception){
                println(e.stackTraceToString())
            }
        }
    }
}