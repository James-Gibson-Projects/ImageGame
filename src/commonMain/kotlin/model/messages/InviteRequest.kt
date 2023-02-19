package model.messages

import kotlinx.serialization.Serializable

@Serializable
sealed class InviteRequest: WebsocketRequest(){
    @Serializable
    data class SendRequest(val name: String): InviteRequest()
    @Serializable
    class Refresh: InviteRequest()
}

@Serializable
sealed class InviteResponse: WebsocketResponse() {

    @Serializable
    data class Success(val state: InvitationsState): InviteResponse()

    @Serializable
    data class Error(val message: String): InviteResponse()
}
@Serializable
data class InvitationsState(val outgoing: List<String>, val incoming: List<String>)
