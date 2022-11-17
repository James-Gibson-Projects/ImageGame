package presentation.views

import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import org.jetbrains.compose.web.dom.*
import presentation.components.DefaultButton
import presentation.components.DefaultPasswordField
import presentation.components.DefaultTextField
import presentation.components.LoginLayout
import presentation.view_models.LoginViewModel


@Composable
fun LoginView(viewModel: LoginViewModel){
    var username by remember{ viewModel.usernameState }
    var password by remember{ viewModel.passwordState }
    var loginFailed by remember { viewModel.loginFailedState }
    LoginLayout{
        Div(attrs = { classes("-space-y-px rounded-md shadow-sm".split(" ")) }) {
            DefaultTextField(username, "Username", "username") { username = it }
            DefaultPasswordField(password, "password") { password = it }
        }
        DefaultButton("Sign-In") { viewModel.login() }
        if(loginFailed) Div(attrs = { classes("text-red-800") }){
            Text("Failed To Login...")
        }
        Div({classes("flex", "flex-row", "gap-2")}){
            Text("Don't have an account? ")
            NavLink(
                to = "/register",
                attrs = { classes("text-blue-500", "hover:underline") }
            ){
                Text("Sign-Up Here")
            }
        }
    }
}