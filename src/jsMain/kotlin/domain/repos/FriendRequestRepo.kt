package domain.repos

import kotlinx.coroutines.flow.Flow
import model.InvitationsState

interface FriendRequestRepo {
    fun inviteUser(username: String)
    fun acceptInvite(username: String)
    fun observeFriendRequest(): Flow<List<String>>
}