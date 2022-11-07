package data.repo

import data.database.Remote
import domain.repos.InviteRepo
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import model.InvitationsState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InviteRepoImpl: InviteRepo, KoinComponent {
    private val scope = MainScope()
    private val remote by inject<Remote>()
    private var session: DefaultClientWebSocketSession? = null

    val launchJob = MainScope().async {
            session = remote.client.webSocketSession(
                method = HttpMethod.Get,
                host = "127.0.0.1",
                port = 8080,
                path = "/api/invites"
            )
        }

    override fun inviteUser(username: String) { scope.launch {
        try{ session!!.send(username) } catch (e: Exception) { println(e.stackTraceToString()) }

    } }
    override fun acceptInvite(username: String) { scope.launch { session!!.send(username) } }
    override fun observeNotifications(): Flow<InvitationsState> {
        return flow {
            emit(InvitationsState(emptyList(), emptyList()))
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