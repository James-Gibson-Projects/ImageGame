package presentation.views

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import presentation.components.DefaultButton
import presentation.view_models.LoginViewModel


@Composable
fun LoginView(viewModel: LoginViewModel){
    var username by remember{ viewModel.usernameState }
    var password by remember{ viewModel.passwordState }
    Div {
        TextInput(username){
            onInput { username = it.value } }
        Br()
        PasswordInput(password){
            onInput { password = it.value }
        }
        Br()
        DefaultButton("Login"){ viewModel.login() }
    }
}