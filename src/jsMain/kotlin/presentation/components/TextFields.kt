package presentation.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.forId
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.*

@Composable
fun DefaultTextField(
    text: String,
    label: String,
    id: String,
    onInput: (String) -> Unit,
){
    Div {
        Label(attrs = { classes("sr-only"); forId(id) }) { Text(label) }
        TextInput(text) {
            classes("relative block w-full appearance-none rounded-none rounded-t-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm".split(" "))
            this.onInput { onInput(it.value) }
            id(id)
            placeholder(label)
        }
    }
}
@Composable
fun DefaultPasswordField(
    text: String,
    id: String,
    label: String = "Password",
    onInput: (String) -> Unit,

    ){
    Div {
        Label(attrs = { classes("sr-only"); forId(id) }) { Text(label) }
        PasswordInput(text) {
            classes("relative block w-full appearance-none rounded-none rounded-b-md border border-gray-300 px-3 py-2 text-gray-900 placeholder-gray-500 focus:z-10 focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm".split(" "))
            id(id)
            placeholder(label)
            this.onInput { onInput(it.value) }
        }
    }
}