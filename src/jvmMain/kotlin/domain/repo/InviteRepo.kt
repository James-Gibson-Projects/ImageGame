package domain.repo

interface InviteRepo{
    fun sendInvite(fromUsername: String, toUsername: String)
    fun getUserIncomingInvites(name: String): List<String>
    fun getUserOutgoingInvites(name: String): List<String>
    fun acceptInvite(fromUsername: String, toUsername: String)
}