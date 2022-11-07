package presentation

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.BrowserRouter
import app.softwork.routingcompose.HashRouter
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Router
import org.jetbrains.compose.web.dom.Text
import presentation.components.InvitesComponent
import presentation.components.LoginLayout
import presentation.view_models.InvitesViewModel
import presentation.view_models.LoginViewModel
import presentation.view_models.RegistrationViewModel
import presentation.views.*

class AppRoot{
    @Composable
    fun Root(){
        HashRouter("/home") {
            route("/") { HomeView() }
            route("/home") { HomeView() }
            route("/board"){ BoardView() }
            route("/register") {
                val viewModel = RegistrationViewModel(Router.current)
                RegistrationView(viewModel)
            }
            route("/login") {
                val viewModel = LoginViewModel(Router.current)
                LoginView(viewModel)
            }
            route("/message"){ NotificationComponent() }
            route("/invites"){
                val viewModel = InvitesViewModel()
                InvitesComponent(viewModel)
            }
            route("/test"){
                LoginLayout()
            }
            noMatch {
                NavLink("/home") {
                    Text("Home")
                }
                Text("No page found")
                Text(parameters.toString())
            }
        }
    }
}


