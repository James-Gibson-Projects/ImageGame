package model

import kotlinx.serialization.Serializable

@Serializable
class InviteRequest(val user: String, val type: Type) {
    enum class Type {
        Request,
        Accept
    }
}


@Serializable
data class InvitationsState(val outgoing: List<String>, val incoming: List<String>)
