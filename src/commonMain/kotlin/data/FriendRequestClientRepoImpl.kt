package data

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import model.messages.InviteRequest
import model.messages.InviteResponse
import org.koin.core.component.KoinComponent

class FriendRequestClientRepoImpl(private val webSocket: WebSocket): FriendRequestClientRepo, KoinComponent {

    override suspend fun inviteUser(username: String) {
        webSocket.sendRequest(InviteRequest(username))
    }

    override suspend fun observeIncomingFriendRequests() = webSocket
        .observerResponses()
        .filter { it is InviteResponse.Success }
        .map { (it as InviteResponse.Success).state.incoming }

    override suspend fun observeOutgoingFriendRequests() = webSocket
        .observerResponses()
        .filter { it is InviteResponse.Success }
        .map { (it as InviteResponse.Success).state.outgoing }

    override suspend fun observeErrors() = webSocket
        .observerResponses()
        .filter { it is InviteResponse.Error }
        .map { (it as InviteResponse.Error).message }

}