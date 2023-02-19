package data.repo

import data.websocket.WebSocket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import model.messages.InvitationsState
import model.messages.InviteRequest
import model.messages.InviteResponse
import org.koin.core.component.KoinComponent

class FriendRequestClientRepoImpl(private val webSocket: WebSocket): FriendRequestClientRepo, KoinComponent {

    override suspend fun inviteUser(username: String) {
        webSocket.sendRequest(InviteRequest.SendRequest(username))
    }

    override fun observeFriendState(): Flow<InvitationsState> = webSocket
        .observeResponses(InviteResponse.Success::class, InviteRequest.Refresh())
        .map { it.state }

    override suspend fun observeErrors() = webSocket
        .observeResponses(InviteResponse.Error::class)
        .map { it.message }

}