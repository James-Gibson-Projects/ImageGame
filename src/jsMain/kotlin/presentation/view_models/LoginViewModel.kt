package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import app.softwork.routingcompose.Router
import data.repo.FriendRequestClientRepo
import data.repo.LoginRepo
import kotlinx.coroutines.*
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(private val router: Router): KoinComponent {
    private val repo by inject<LoginRepo>()
    private val handler = CoroutineExceptionHandler { _, e -> loginFailedState.value = true }
    private val scope = MainScope() + handler
    val usernameState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val loginFailedState = mutableStateOf(false)
    fun login(){
        scope.launch {
            console.log("logging")
            repo.login(usernameState.value, passwordState.value)
            console.log("called")
            router.navigate("/home")
        }
    }
}