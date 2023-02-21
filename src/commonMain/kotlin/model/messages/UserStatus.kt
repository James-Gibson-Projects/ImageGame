package model.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserStatus {
    Online,
    Offline,
    FriendRequestSent,
    FriendRequestReceived
}

@Serializable
data class FriendState(val users: List<UserData>): WebsocketResponse()

@Serializable
data class UserData(val name: String, val status: UserStatus)