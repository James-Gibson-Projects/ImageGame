package data.repo

import kotlinx.coroutines.flow.Flow
import model.messages.GameInviteResponse

interface GameRequestClientRepo {
    fun observeInvites(): Flow<GameInviteResponse>
    suspend fun sendRequest(to: String)
    suspend fun acceptRequest(from: String)
}