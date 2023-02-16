package data.repo

import domain.repo.InviteRepo

class InviteRepoImpl : InviteRepo {
    override fun sendInvite(fromUsername: String, toUsername: String) {
        TODO("Not yet implemented")
    }

    override fun getUserIncomingInvites(name: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun getUserOutgoingInvites(name: String): List<String> {
        TODO("Not yet implemented")
    }
}