package presentation.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun GameInviteNotification(sender: String, onAccept: () -> Unit, onReject: () -> Unit) {
    Div({
        classes("bg-white", "border", "border-gray-300", "rounded-md", "shadow-md", "p-4", "flex", "items-center", "space-x-4")
        style {
            position(Position.Fixed)
            top(16.px)
            right(16.px)
        }
    }) {
        Text("You have a game invite from $sender.")
        Button({
            classes("ml-auto", "text-white", "bg-green-500", "hover:bg-green-600")
            onClick { onAccept() }
        }) {
            Text("Accept")
        }
        Button({
            classes("text-gray-500", "hover:text-gray-600")
            onClick { onReject() }
        }) {
            Text("Reject")
        }
    }
}
