package presentation.view_models

import domain.repos.FriendsRepo
import kotlinx.coroutines.flow.catch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendsViewModel: KoinComponent {
    private val repo by inject<FriendsRepo>()
    val invites = repo.observeFriends().catch { throw it }
}