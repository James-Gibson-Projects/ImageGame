package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import domain.repos.InviteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import model.InvitationsState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InvitesViewModel: KoinComponent {
    val errorState = mutableStateOf(false)
    private val repo by inject<InviteRepo>()
    val invites: Flow<InvitationsState> = repo.observeNotifications().catch { throw it }
    val toInviteState = mutableStateOf("")
    fun sendInvite(toUsername: String){
        try {
            repo.inviteUser(toUsername)
            errorState.value = false
        } catch (e: Exception){ errorState.value = true }
        toInviteState.value = ""
    }
    fun acceptInvite(fromUsername: String){ repo.acceptInvite(fromUsername) }
}