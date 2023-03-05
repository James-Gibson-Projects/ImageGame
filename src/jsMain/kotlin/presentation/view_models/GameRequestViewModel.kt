package presentation.view_models

import data.repo.GameInviteRequestClientRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameRequestViewModel: KoinComponent {
    private val repo by inject<GameInviteRequestClientRepo>()
    private val scope = CoroutineScope(Dispatchers.Default)
    val gameInviteFlow = repo.observeInvites()

    fun sendRequest(name: String){
        scope.launch { repo.sendRequest(name) }
    }

    fun acceptRequest(name: String){
        scope.launch { repo.acceptRequest(name) }
    }
}