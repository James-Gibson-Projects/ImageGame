package presentation.views


import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.*
import presentation.view_models.LoginViewModel
import presentation.view_models.RegistrationViewModel

@Composable
fun RegistrationView(viewModel: RegistrationViewModel){
    var username by remember{ viewModel.usernameState }
    var password by remember{ viewModel.passwordState }
    Div {
        TextInput(username){
            onInput { username = it.value }
        }
        Br()
        PasswordInput(password){
            onInput { password = it.value }
        }
        Br()
        Button({ onClick { viewModel.register() } }){
            Text("Register")
        }
    }
}

