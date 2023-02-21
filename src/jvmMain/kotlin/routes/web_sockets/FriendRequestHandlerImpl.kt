package routes.web_sockets

import domain.repo.FriendRequestRepo

import io.ktor.server.websocket.*
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import domain.model.User
import domain.model.UserSession
import domain.repo.FriendRepo
import model.messages.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestHandlerImpl: FriendRequestHandler, KoinComponent {

    private val friendRequestRepo by inject<FriendRequestRepo>()
    private val friendRepo by inject<FriendRepo>()

    override suspend fun shouldHandle(request: WebsocketRequest): Boolean {
        return request is FriendRequest
    }

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) {}

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
            if(request is FriendRequest.SendRequest) {
                val otherUsername = request.name
                try {
                    friendRequestRepo.sendRequest(session.username, otherUsername)
                    sendUpdatedState(session.username)
                    val otherConnection = connections[otherUsername]
                        ?: return
                    otherConnection.webSocketSession.sendUpdatedState(otherUsername)
                } catch (_: InviteAlreadySentException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("Friend request already sent"))
                } catch (_: UserNotFoundException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("User not found: $otherUsername"))
                } catch (_: SelfInviteException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("You can't send a friend request to yourself"))
                }
            } else if(request is FriendRequest.Refresh){
                sendUpdatedState(session.username)
            }
        }

    private suspend fun DefaultWebSocketServerSession.sendUpdatedState(name: String) {
        val outgoing: List<Pair<String, UserStatus>> = friendRequestRepo.getUserOutgoingInvites(name).map { it to UserStatus.FriendRequestSent() }
        val incoming: List<Pair<String, UserStatus>> = friendRequestRepo.getUserIncomingInvites(name).map { it to UserStatus.FriendRequestReceived() }
        val friends: List<Pair<String, UserStatus>> = friendRepo.getFriends(name).map { it.username to if(it.isOnline) UserStatus.Friend.Online() else UserStatus.Friend.Offline() }
        sendSerialized(FriendState((outgoing + incoming + friends).toMap()))
    }

    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) {}
}

