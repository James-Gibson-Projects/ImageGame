package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import app.softwork.routingcompose.Router
import data.repo.LoginRepo
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(private val router: Router): KoinComponent {
    private val repo by inject<LoginRepo>()
    private val handler = CoroutineExceptionHandler { _, e -> loginFailedState.value = true }
    private val scope = MainScope() + handler
    val usernameState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val loginFailedState = mutableStateOf(false)
    fun login(username: String, password: String) {
        scope.launch {
            try {
                repo.login(username , password)
                router.navigate("/home")
            } catch (e: Exception){
                loginFailedState.value = true
            }
        }
    }
}