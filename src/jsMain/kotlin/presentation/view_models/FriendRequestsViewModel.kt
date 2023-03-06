package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import data.repo.FriendClientRepo
import data.repo.GameInviteRequestClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestsViewModel: KoinComponent {
    private val friendRepo by inject<FriendClientRepo>()
    private val gameRequestRepo by inject<GameInviteRequestClientRepo>()
    private val scope = CoroutineScope(Dispatchers.Default)
    val friendRequestsStateFlow = friendRepo.observeFriendState()
    val textBoxState = mutableStateOf("")
    val errorFlow = friendRepo.observeErrorState()
    fun sendFriendRequest(name: String) {
        scope.launch { friendRepo.inviteUser(name) }
    }

    fun sendGameRequest(name: String) {
        scope.launch { gameRequestRepo.sendRequest(name) }
    }

    fun refresh(){
        scope.launch { friendRepo.refresh() }
    }
}