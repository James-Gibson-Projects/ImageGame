package presentation.view_models

import domain.repos.FriendRequestRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FriendRequestsViewModel: KoinComponent {
    private val repo by inject<FriendRequestRepo>()
    val receivedFriendRequests = repo.observeFriendRequest()
}