package presentation.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/*
@Composable
fun SideBar(viewModel: FriendsViewModel){
    val friends by viewModel.invites.collectAsState(listOf())
    Div({classes("absolute inset-y-0 right-0 w-32 bg-grey-300 flex flex-col".split(" "))}) {
        friends.forEach {
            FriendCard(it)
        }
    }
}
@Composable
fun FriendCard(activeStatus: Pair<String, Long>){
    Div ({classes("flex flex-row".split(" "))}){
        Text(activeStatus.first)
        Text("last online: ${activeStatus.second / 60000}m ago")
    }
}

 */