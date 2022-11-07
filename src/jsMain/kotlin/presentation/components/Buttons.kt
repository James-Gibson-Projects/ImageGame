package presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.attributes.forId

import org.jetbrains.compose.web.dom.*

/**
 * https://tailwindui.com/components/application-ui/forms/sign-in-forms
 */

@Composable
fun DefaultButton(
    text: String,
    action: () -> Unit
){
    Button({
        classes(("group relative flex w-full justify-center rounded-md" +
                " border border-transparent bg-indigo-600 py-2 px-4 text-sm" +
                " font-medium text-white hover:bg-indigo-700 focus:outline-none" +
                " focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2").split(" "))
        onClick{action()}
    }) {
        Text(text)
    }
}

@Composable
fun LoginLayout(){
    Div({classes("flex min-h-full items-center justify-center py-12 px-4 sm:px-6 lg:px-8".split(" "))}) {
        Div({classes("w-full max-w-md space-y-8".split(" "))}) {
            Form(attrs = {classes("mt-8 space-y-6".split(" "))}) {
                Div(attrs = {classes("-space-y-px rounded-md shadow-sm".split(" "))}) {
                    var username by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    DefaultTextField(username, "Username", "username") {username = it}
                    DefaultPasswordField(password, "password"){password = it}
                }
            }
        }
    }
}

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
            this.onInput { onInput(it.value) }
        }
    }
}