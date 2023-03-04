package model.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FriendRequest: WebsocketRequest(){
    @Serializable
    data class SendRequest(val name: String): FriendRequest()
    @Serializable
    class Refresh: FriendRequest()
}

@Serializable
sealed class FriendResponse: WebsocketResponse() {

    @Serializable
    class Success: FriendResponse()

    @Serializable
    data class Error(val message: String): FriendResponse()
}
