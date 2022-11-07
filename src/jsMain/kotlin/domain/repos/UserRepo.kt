package domain.repos

import model.UserCredentials

interface UserRepo {
    suspend fun login(details: UserCredentials): Boolean
    suspend fun register(details: UserCredentials): Boolean
}