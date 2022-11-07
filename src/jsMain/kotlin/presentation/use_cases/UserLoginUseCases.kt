package presentation.use_cases

import app.softwork.routingcompose.Router
import model.UserCredentials

interface UserLoginUseCases {
    suspend fun login(details: UserCredentials, router: Router)
    suspend fun register(details: UserCredentials, router: Router)
}