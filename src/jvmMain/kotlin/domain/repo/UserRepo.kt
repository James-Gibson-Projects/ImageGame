package domain.repo

import data.db.schema.UserSession

interface UserRepo {
    fun createUser(name: String, pass: String): UserSession?
    suspend fun loginUser(name: String, password: String): UserSession?
    fun logoutUser(session: UserSession)
    fun verifySession(session: UserSession): UserSession?
}