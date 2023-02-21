package presentation

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.HashRouter
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Router
import org.jetbrains.compose.web.dom.Text
import presentation.components.InvitesComponent
import presentation.components.TopBar
import presentation.view_models.FriendRequestsViewModel
import presentation.view_models.LoginViewModel
import presentation.view_models.RegistrationViewModel
import presentation.views.*

class AppRoot{
    @Composable
    fun Root(){
        HashRouter("/login") {
            val router = Router.current
            route("/home") {
                // HomeView(FriendsViewModel())
                HomeView(FriendRequestsViewModel())
            }
            route("/register") {
                val viewModel = RegistrationViewModel(router)
                RegistrationView(viewModel)
            }
            route("/login") {
                val viewModel = LoginViewModel(router)
                LoginView(viewModel)
            }
            route("/invites"){
                val viewModel = FriendRequestsViewModel()
                InvitesComponent(viewModel)
            }
            route("/test"){
                TopBar()
                //LoginLayout()
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


