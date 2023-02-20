package data.repo

interface LoginRepo {
    suspend fun login(username: String, password: String)
    suspend fun register(username: String, password: String)
    suspend fun logout()
}