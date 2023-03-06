package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.GameInviteResponse

interface GameInviteRequestClientRepo {
    fun observeInvites(): Flow<GameInviteResponse>
    suspend fun sendRequest(to: String)
    suspend fun acceptRequest(from: String)
}