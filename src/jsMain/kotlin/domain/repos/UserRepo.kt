package domain.repos

import model.UserCredentials

interface UserRepo {
    suspend fun login(details: UserCredentials)
    suspend fun register(details: UserCredentials)
}