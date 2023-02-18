package domain.repo

interface FriendRequestRepo {
    fun sendRequest(fromUsername: String, toUsername: String)
    fun getUserIncomingInvites(name: String): List<String>
    fun getUserOutgoingInvites(name: String): List<String>
    fun acceptRequest(fromUsername: String, toUsername: String)
}