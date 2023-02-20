package presentation.views

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import org.jetbrains.compose.web.dom.*
import presentation.view_models.FriendRequestsViewModel

/*
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
}

 */

@Composable
fun HomeView(friendRequestsViewModel: FriendRequestsViewModel) = Div( { classes("fixed", "right-0", "h-screen", "bg-gray-100", "flex", "flex-col", "p-4") } ) {
    // val
    Div( { classes("text-lg", "font-bold", "mb-4") } ) {
        Text("ONLINE FRIENDS")
    }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {
    }
    Div( { classes("text-lg", "font-bold", "mt-4", "mb-4") } ) { Text("INCOMING FRIEND REQUESTS") }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {

    }
}

@Composable
fun Friend(){
    Label( attrs = { classes("flex", "items-center", "py-2", "px-3", "rounded-md", "hover:bg-gray-200") } ) {
        Text("Dave")
        Button( { classes("ml-auto", "rounded-md", "bg-green-500", "text-white", "py-1", "px-2", "hover:bg-green-600") } ) { Text("Invite") }
    }
}

@Composable
fun FriendRequest(name: String, action: () -> String){
    Label( attrs = { classes("flex", "items-center", "py-2", "px-3", "rounded-md", "hover:bg-gray-200") } ) {
        Text(name)
        Button( { classes("ml-auto", "rounded-md", "bg-green-500", "text-white", "py-1", "px-2", "hover:bg-green-600"); onClick { action() } } ) {
            Text("Accept")
        }
    }
}
/*
@Composable
fun HomeView(friendsViewModel: FriendsViewModel){
    TopBar()
    SideBar(friendsViewModel)
}
 */
