package model

import kotlinx.serialization.Serializable

@Serializable
sealed class ActiveStatus{
    @Serializable
    object Online: ActiveStatus()
    @Serializable
    class Offline(val lastOnlineInMs: Long): ActiveStatus()
}