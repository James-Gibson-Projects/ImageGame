package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import data.repo.FriendClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestsViewModel: KoinComponent {
    private val repo by inject<FriendClientRepo>()
    private val scope = CoroutineScope(Dispatchers.Default)
    val friendRequestsStateFlow = repo.observeFriendState()
    val textBoxState = mutableStateOf("")
    val errorFlow = repo.observeErrorState()
    fun sendFriendRequest(){
        scope.launch { repo.inviteUser(textBoxState.value) }
    }
    fun acceptFriendRequest(name: String){
        scope.launch { repo.inviteUser(name) }
    }
}