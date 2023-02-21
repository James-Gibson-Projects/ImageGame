package model.messages

import kotlinx.serialization.Serializable

@Serializable
sealed class UserStatus{

    @Serializable
    sealed class Friend: UserStatus(){

        @Serializable
        class Online: UserStatus()

        @Serializable
        class Offline: UserStatus()

    }

    @Serializable
    class FriendRequestSent: UserStatus()

    @Serializable
    class FriendRequestReceived: UserStatus()
}

@Serializable
class FriendState(val users: Map<String, UserStatus>): WebsocketResponse()