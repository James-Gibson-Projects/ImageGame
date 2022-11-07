package presentation.components

import androidx.compose.runtime.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.UserCredentials
import org.jetbrains.compose.web.dom.*

@Composable
fun LoginFields(
    text: String,
    action: suspend (UserCredentials) -> Unit
){
    val mainScope = MainScope()
    var username by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }
    Div {
        TextInput(username){
            onInput { username = it.value }
        }
        Br()
        PasswordInput(password){
            onInput { password = it.value }
        }
        Br()
        DefaultButton(text){
            mainScope.launch{ action(UserCredentials(username, password)) }
        }
    }
}