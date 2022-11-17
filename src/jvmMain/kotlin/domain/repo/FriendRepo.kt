package domain.repo

import data.db.schema.UserLastOnline

interface FriendRepo {
    fun getFriends(username: String): List<UserLastOnline>
    fun deleteFriendship(firstUsername: String, secondUsername: String)
}
