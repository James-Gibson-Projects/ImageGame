package presentation.view_models

import androidx.compose.runtime.*
import app.softwork.routingcompose.Router
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.use_cases.UserLoginUseCases

class LoginViewModel(private val router: Router): KoinComponent {
    private val useCases by inject<UserLoginUseCases>()
    private val scope = MainScope()
    var usernameState = mutableStateOf("")
    var passwordState = mutableStateOf("")
    fun login(){
        scope.launch {
            useCases.login(
                UserCredentials(usernameState.value, passwordState.value),
                router
            )
        }
    }
}