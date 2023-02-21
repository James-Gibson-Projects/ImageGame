package presentation.views

import androidx.compose.runtime.*
import model.messages.FriendState
import model.messages.UserStatus
import org.jetbrains.compose.web.dom.*
import presentation.components.DefaultButton
import presentation.components.DefaultTextField
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
    val friendState by friendRequestsViewModel.friendRequestsStateFlow.collectAsState(FriendState(emptyList()))
    var textFieldValue by remember { friendRequestsViewModel.textBoxState }
    Div( { classes("text-lg", "font-bold", "mb-4") } ) { Text("ONLINE FRIENDS") }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {
        friendState.users
            .filter { it.status == UserStatus.Online || it.status == UserStatus.Offline }
            .forEach { Friend(it.name) }
    }
    Div( { classes("text-lg", "font-bold", "mt-4", "mb-4") } ) { Text("INCOMING FRIEND REQUESTS") }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {
        friendState.users
            .filter { it.status == UserStatus.FriendRequestReceived }
            .forEach { FriendRequest(it.name){ friendRequestsViewModel.acceptFriendRequest(it.name) } }
    }
    DefaultTextField(textFieldValue, "Username", "SendFriendRequestTextField"){ textFieldValue = it }
    DefaultButton("Send Friend Request"){ friendRequestsViewModel.sendFriendRequest() }
}

@Composable
fun Friend(name: String){
    Label( attrs = { classes("flex", "items-center", "py-2", "px-3", "rounded-md", "hover:bg-gray-200") } ) {
        Text(name)
        Button( { classes("ml-auto", "rounded-md", "bg-green-500", "text-white", "py-1", "px-2", "hover:bg-green-600") } ) { Text("Invite") }
    }
}

@Composable
fun FriendRequest(name: String, action: () -> Unit){
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
