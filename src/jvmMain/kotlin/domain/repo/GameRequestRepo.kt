package domain.repo

interface GameRequestRepo {
    fun sendRequest(fromUsername: String, toUsername: String)
    fun getUserIncomingInvites(name: String): List<String>
    fun getUserOutgoingInvites(name: String): List<String>
    fun acceptRequest(fromUsername: String, toUsername: String): String
}