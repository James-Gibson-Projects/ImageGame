package domain.repos

import kotlinx.coroutines.flow.Flow
import model.ActiveStatus

interface FriendsRepo {
    fun observeFriends(): Flow<List<Pair<String, Long>>>
}