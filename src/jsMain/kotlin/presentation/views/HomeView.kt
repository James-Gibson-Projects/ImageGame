package presentation.views

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import presentation.components.SideBar
import presentation.components.TopBar
import presentation.view_models.FriendsViewModel

@Composable
fun HomeView(){
    NavLink("/register") { Text("Register") }
    Br()
    NavLink("/login") { Text("Login") }
    Br()
    NavLink("/board") { Text("Chess") }
    Br()
    NavLink("/message") { Text("Message") }
    Br()
    NavLink("/invites") { Text("Invites") }
    Br()
    NavLink("/test") { Text("Test") }
}

/*
@Composable
fun HomeView(friendsViewModel: FriendsViewModel){
    TopBar()
    SideBar(friendsViewModel)
}
 */
