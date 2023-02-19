package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.InvitationsState

interface FriendRequestClientRepo {
    suspend fun inviteUser(username: String)
    fun observeFriendState(): Flow<InvitationsState>
    suspend fun observeErrors(): Flow<String>
}
