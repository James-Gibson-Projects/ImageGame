package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.FriendState
import model.messages.InvitationsState
import model.messages.InviteResponse

interface FriendClientRepo {
    suspend fun inviteUser(username: String)
    suspend fun refresh()
    fun observeFriendState(): Flow<FriendState>
    fun observeErrorState(): Flow<InviteResponse>
}
