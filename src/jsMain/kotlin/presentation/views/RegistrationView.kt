package presentation.views


import androidx.compose.runtime.*
import app.softwork.routingcompose.NavLink
import org.jetbrains.compose.web.dom.*
import presentation.components.DefaultButton
import presentation.components.DefaultPasswordField
import presentation.components.DefaultTextField
import presentation.components.LoginLayout
import presentation.view_models.LoginViewModel
import presentation.view_models.RegistrationViewModel

@Composable
fun RegistrationView(viewModel: RegistrationViewModel){
    var username by remember{ viewModel.usernameState }
    var password by remember{ viewModel.passwordState }
    var signupFailed by remember { viewModel.signupFailedState }
    LoginLayout{
        Div(attrs = { classes("-space-y-px rounded-md shadow-sm".split(" ")) }) {
            DefaultTextField(username, "Username", "username") { username = it }
            DefaultPasswordField(password, "password") { password = it }
        }
        DefaultButton(
            text = "Sign-Up",
            bgColor= "bg-green-600",
            hoverColor= "hover:bg-green-700",
            ringColor= "focus:ring-green-500",
            iconColor= "text-green-500",
            iconHoverColor= "group-hover:text-green-500",
) { viewModel.register(username, password) }
        if(signupFailed) Div(attrs = { classes("text-red-500") }){
            Text("Failed To sign-up...")
        }
        Div({classes("flex", "flex-row", "gap-2")}){
            Text("Already have an account? ")
            NavLink(
                to = "/login",
                attrs = { classes("text-blue-800", "hover:underline") }
            ){
                Text("Login Here")
            }
        }
    }
}

