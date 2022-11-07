package domain.use_cases

import app.softwork.routingcompose.Router
import domain.repos.UserRepo
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.use_cases.UserLoginUseCases

class UserLoginUseCasesImpl: UserLoginUseCases, KoinComponent {
    private val repo by inject<UserRepo>()
    override suspend fun login(details: UserCredentials, router: Router) {
        if(repo.login(details)) router.navigate("/home")
    }
    override suspend fun register(details: UserCredentials, router: Router) {
        if(repo.register(details)) router.navigate("/home")
    }
}