package routes.web_sockets

import domain.repo.FriendRequestRepo

import io.ktor.server.websocket.*
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import domain.model.UserSession
import model.messages.InvitationsState
import model.messages.InviteRequest
import model.messages.InviteResponse
import model.messages.WebsocketRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestHandlerImpl: FriendRequestHandler, KoinComponent {

    val repo by inject<FriendRequestRepo>()

    override suspend fun shouldHandle(request: WebsocketRequest): Boolean {
        return request is InviteRequest
    }

    override suspend fun DefaultWebSocketServerSession.onConnect(session: UserSession) {}

    override suspend fun DefaultWebSocketServerSession.handle(session: UserSession, request: WebsocketRequest) {
            if(request is InviteRequest.SendRequest) {
                val otherUsername = request.name
                try {
                    repo.sendRequest(session.username, otherUsername)
                    sendSerialized<InviteResponse>(
                        InviteResponse.Success(
                            InvitationsState(
                                repo.getUserOutgoingInvites(session.username),
                                repo.getUserIncomingInvites(session.username)
                            )
                        )
                    )
                    val otherConnection = connections[otherUsername]
                        ?: return
                    otherConnection.webSocketSession.sendSerialized<InviteResponse>(
                        InviteResponse.Success(
                            InvitationsState(
                                repo.getUserOutgoingInvites(otherConnection.userSession.username),
                                repo.getUserIncomingInvites(otherConnection.userSession.username)
                            )
                        )
                    )
                } catch (_: InviteAlreadySentException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("Friend request already sent"))
                } catch (_: UserNotFoundException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("User not found: $otherUsername"))
                } catch (_: SelfInviteException) {
                    sendSerialized<InviteResponse>(InviteResponse.Error("You can't send a friend request to yourself"))
                }
            } else if(request is InviteRequest.Refresh){
                sendSerialized<InviteResponse>(
                    InviteResponse.Success(
                        InvitationsState(
                            repo.getUserOutgoingInvites(session.username),
                            repo.getUserIncomingInvites(session.username)
                        )
                    )
                )
            }
        }
    override suspend fun DefaultWebSocketServerSession.onClose(session: UserSession) {}
}

