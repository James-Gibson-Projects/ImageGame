package presentation.view_models

import data.repo.FriendRequestClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestsViewModel: KoinComponent {
    private val repo by inject<FriendRequestClientRepo>()
    private val scope = CoroutineScope(Dispatchers.Default)
    val friendRequestsStateFlow = repo.observeFriendState()
    val errorFlow = repo.observeErrors()
    fun sendFriendRequest(toUsername: String){
        scope.launch { repo.inviteUser(toUsername) }
    }
}