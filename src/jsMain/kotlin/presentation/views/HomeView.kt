package presentation.views

import androidx.compose.runtime.*
import app.softwork.routingcompose.RouteBuilder
import app.softwork.routingcompose.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import model.messages.*
import org.jetbrains.compose.web.dom.*
import presentation.components.DefaultButton
import presentation.components.DefaultTextField
import presentation.components.GameInviteNotification
import presentation.view_models.FriendRequestsViewModel
import presentation.view_models.GameRequestViewModel
import presentation.view_models.GameViewModel

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
fun RouteBuilder.HomeView(
    friendRequestsViewModel: FriendRequestsViewModel,
    gameRequestViewModel: GameRequestViewModel,
    scope: CoroutineScope
){
    Div(attrs = { classes("flex") }){
        NotificationOverlay(gameRequestViewModel)
        Div(attrs = { classes("flex-grow") }){
            route("/game"){
                string {
                    val viewModel = remember { GameViewModel(it, scope).also { it.refresh() } }
                    ChessBoardView(viewModel)
                }
            }
        }
        Div(attrs = { classes("flex-initial", "w-64") }){
            SideBar(friendRequestsViewModel)
        }
    }
}

@Composable
fun NotificationOverlay(viewModel: GameRequestViewModel){
    val notificationResponse by viewModel.gameInviteFlow.collectAsState(null)
    var notification: String? by remember { mutableStateOf(null) }
    val router = Router.current
    if(notificationResponse is GameInviteResponse.Received){
        notification = (notificationResponse as GameInviteResponse.Received).from
    } else if (notificationResponse is GameInviteResponse.Started) {
        notification = null
        router.navigate("/home/game/${(notificationResponse as GameInviteResponse.Started).gameId}")
    }
    notification?.let {
        GameInviteNotification(it, { viewModel.acceptRequest(it) }, {})
    }
}

@Composable
fun SideBar(friendRequestsViewModel: FriendRequestsViewModel) = Div( { classes("h-screen", "bg-gray-100", "flex", "flex-col", "p-4") } ) {
    val friendState by friendRequestsViewModel.friendRequestsStateFlow.collectAsState(FriendState(emptyList()))
    var textFieldValue by remember { friendRequestsViewModel.textBoxState }
    val error by friendRequestsViewModel.errorFlow.collectAsState(FriendResponse.Success)
    Div( { classes("text-lg", "font-bold", "mb-4") } ) { Text("ONLINE FRIENDS") }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {
        friendState.users
            .filter { it.status == UserStatus.Online || it.status == UserStatus.Offline }
            .forEach { Friend(it.name){ friendRequestsViewModel.sendGameRequest(it.name) } }
    }
    Div( { classes("text-lg", "font-bold", "mt-4", "mb-4") } ) { Text("INCOMING FRIEND REQUESTS") }
    Div( { classes("flex-1", "flex", "flex-col", "space-y-2") } ) {
        friendState.users
            .filter { it.status == UserStatus.FriendRequestReceived }
            .forEach { FriendRequest(it.name){ friendRequestsViewModel.sendFriendRequest(it.name) } }
    }

    Div( { classes("flex-4", "flex", "flex-col", "space-y-2") } ) {
        if(error is FriendResponse.Error) Text((error as FriendResponse.Error).message)
        DefaultTextField(textFieldValue, "Username", "SendFriendRequestTextField"){ textFieldValue = it }
        DefaultButton("Send Friend Request"){ friendRequestsViewModel.sendFriendRequest(textFieldValue); textFieldValue = "" }
        DefaultButton("Refresh"){ friendRequestsViewModel.refresh() }
    }
}

@Composable
fun Friend(name: String, action: () -> Unit){
    Label( attrs = { classes("flex", "items-center", "py-2", "px-3", "rounded-md", "hover:bg-gray-200") } ) {
        Text(name)
        Button( { classes("ml-auto", "rounded-md", "bg-green-500", "text-white", "py-1", "px-2", "hover:bg-green-600"); onClick { action() } } ) {
            Text("Invite")
        }
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
