package presentation.components

import androidx.compose.runtime.*
import model.messages.InvitationsState
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput
import presentation.view_models.InvitesViewModel


@Composable
fun InvitesComponent(viewModel: InvitesViewModel){
    val invites: InvitationsState by viewModel.invites.collectAsState(InvitationsState(emptyList(), emptyList()))
    var toInvite by remember{ viewModel.toInviteState }
    var error by remember { viewModel.errorState }
    TextInput(toInvite) { onInput { toInvite = it.value } }
    Button(
        attrs = {
            onClick {
                viewModel.sendInvite(toInvite)
            }
        }
    ) {
        Text("Send")
    }
    invites.incoming.forEach {
        Text(it)
        Br()
    }
}

