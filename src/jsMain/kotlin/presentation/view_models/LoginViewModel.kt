package presentation.view_models

import androidx.compose.runtime.mutableStateOf
import app.softwork.routingcompose.Router
import data.repo.FriendRequestClientRepo
import kotlinx.coroutines.*
import model.UserCredentials
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(private val router: Router): KoinComponent {
    private val repo by inject<FriendRequestClientRepo>()
    private val handler = CoroutineExceptionHandler { _, e -> loginFailedState.value = true }
    private val scope = MainScope() + handler
    val usernameState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val loginFailedState = mutableStateOf(false)
    fun login(){
        scope.launch {
            withContext(Dispatchers.Default) {
                repo.login(UserCredentials(usernameState.value, passwordState.value))
            }
            router.navigate("/home")
        }
    }
}