package data

import kotlinx.coroutines.flow.Flow

interface FriendRequestClientRepo {
    suspend fun inviteUser(username: String)
    suspend fun observeIncomingFriendRequests(): Flow<List<String>>
    suspend fun observeErrors(): Flow<String>
    suspend fun observeOutgoingFriendRequests(): Flow<List<String>>
}
