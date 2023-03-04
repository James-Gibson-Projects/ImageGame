package presentation

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.HashRouter
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Router
import data.websocket.WebSocket
import org.jetbrains.compose.web.dom.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.components.TopBar
import presentation.view_models.FriendRequestsViewModel
import presentation.view_models.GameRequestViewModel
import presentation.view_models.LoginViewModel
import presentation.view_models.RegistrationViewModel
import presentation.views.*

class AppRoot: KoinComponent {
    @Composable
    fun Root(){
        HashRouter("/login") {
            val router = Router.current
            route("/home") {
                val websocket: WebSocket by inject()
                websocket.onClose { router.navigate("/login") }
                websocket.connect()
                HomeView(FriendRequestsViewModel(), GameRequestViewModel())
            }
            route("/register") {
                val viewModel = RegistrationViewModel(router)
                RegistrationView(viewModel)
            }
            route("/login") {
                val viewModel = LoginViewModel(router)
                LoginView(viewModel)
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


