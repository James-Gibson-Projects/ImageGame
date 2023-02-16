package domain.repo

import domain.model.UserWithStatus

interface FriendRepo {
    fun getFriends(username: String): List<UserWithStatus>
    fun deleteFriendship(firstUsername: String, secondUsername: String)
}
