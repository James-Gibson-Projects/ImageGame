package model.messages

import kotlinx.serialization.Serializable

@Serializable
data class GameInviteResponse(val from: String): WebsocketResponse()