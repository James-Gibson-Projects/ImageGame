package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.FriendState
import model.messages.InvitationsState

interface FriendClientRepo {
    suspend fun inviteUser(username: String)
    fun observeFriendState(): Flow<FriendState>
}
