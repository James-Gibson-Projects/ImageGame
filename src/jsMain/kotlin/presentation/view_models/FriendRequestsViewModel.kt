package presentation.view_models

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import data.repo.FriendRequestClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.messages.InvitationsState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestsViewModel: KoinComponent {
    private val repo by inject<FriendRequestClientRepo>()
    private val scope = CoroutineScope(Dispatchers.Default)
    val friendRequestsState by repo
        .observeFriendState()
        .collectAsState(InvitationsState(emptyList(), emptyList()))
    fun sendFriendRequest(toUsername: String){
        scope.launch { repo.inviteUser(toUsername) }
    }
}