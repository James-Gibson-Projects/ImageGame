package domain.repos

import kotlinx.coroutines.flow.Flow

interface FriendRequestRepo {
    fun inviteUser(username: String)
    fun acceptInvite(username: String)
    fun observeFriendRequest(): Flow<List<String>>
}