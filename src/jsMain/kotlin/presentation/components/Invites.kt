package presentation.components

import androidx.compose.runtime.*
import model.messages.InvitationsState
import org.jetbrains.compose.web.dom.*
import presentation.view_models.FriendRequestsViewModel


@Composable
fun InvitesComponent(viewModel: FriendRequestsViewModel){
    val invites: InvitationsState? by viewModel
        .friendRequestsStateFlow
        .collectAsState(null)
    val error: String? by viewModel
        .errorFlow
        .collectAsState(null)
    var toInvite by remember{ mutableStateOf("") }
    TextInput(toInvite) { onInput { toInvite = it.value } }
    Button(
        attrs = {
            onClick { viewModel.sendFriendRequest(toInvite) }
        }
    ) {
        Text("Send")
    }
    invites?.incoming?.forEach {
        Text(it)
        Br()
    }

    invites?.incoming?.forEach {
        Div(attrs = { classes("") }) {
            Text(it)
            Br()
        }
    }
}

