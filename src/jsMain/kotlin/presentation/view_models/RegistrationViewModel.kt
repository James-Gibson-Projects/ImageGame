package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import app.softwork.routingcompose.Router
import data.repo.LoginRepo
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.coroutines.*

class RegistrationViewModel(private val router: Router): KoinComponent {
    private val userRepo by inject<LoginRepo>()
    private val handler = CoroutineExceptionHandler { _, e -> signupFailedState.value = true; loadingState.value = false }
    private val scope = MainScope() + handler
    var usernameState = mutableStateOf("")
    var passwordState = mutableStateOf("")
    var signupFailedState = mutableStateOf(false)
    var loadingState = mutableStateOf(false)
    fun register(){
        loadingState.value = true
        scope.launch {
            userRepo.register(usernameState.value, passwordState.value)
        }.invokeOnCompletion { router.navigate("/home") }
    }
}