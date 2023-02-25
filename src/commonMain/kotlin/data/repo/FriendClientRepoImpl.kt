package data.repo

import data.websocket.WebSocket
import kotlinx.coroutines.flow.Flow
import model.messages.FriendRequest
import model.messages.FriendState
import model.messages.FriendResponse
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

    override fun observeErrorState(): Flow<FriendResponse> {
        return webSocket.observeResponses(FriendResponse::class)
    }
}