package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.FriendState
import model.messages.FriendResponse

interface FriendClientRepo {
    suspend fun inviteUser(username: String)
    suspend fun refresh()
    fun observeFriendState(): Flow<FriendState>
    fun observeErrorState(): Flow<FriendResponse>
}
