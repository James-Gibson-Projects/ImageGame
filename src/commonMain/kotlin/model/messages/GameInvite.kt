package model.messages

import kotlinx.serialization.Serializable

@Serializable
sealed class GameInviteRequest: WebsocketRequest(){

    @Serializable
    data class Send(val to: String): GameInviteRequest()

    @Serializable
    data class Accept(val from: String): GameInviteRequest()
}

@Serializable
sealed class GameInviteResponse: WebsocketResponse(){

    @Serializable
    data class Received(val from: String): GameInviteResponse()

    @Serializable
    data class Started(val gameId: String): GameInviteResponse()
}