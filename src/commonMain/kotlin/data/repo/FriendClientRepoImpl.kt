package data.repo

import data.websocket.WebSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.messages.InvitationsState
import model.messages.FriendRequest
import model.messages.FriendState
import model.messages.InviteResponse
import org.koin.core.component.KoinComponent

class FriendClientRepoImpl(private val webSocket: WebSocket): FriendClientRepo, KoinComponent {

    override suspend fun inviteUser(username: String) {
        webSocket.sendRequest(FriendRequest.SendRequest(username))
    }

    override suspend fun refresh() {
        webSocket.sendRequest(FriendRequest.Refresh())
    }

    override fun observeFriendState(): Flow<FriendState> {
        return webSocket.observeResponses(FriendState::class, FriendRequest.Refresh())
    }

    override fun observeErrorState(): Flow<InviteResponse> {
        return webSocket.observeResponses(InviteResponse::class)
    }
}