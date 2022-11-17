package presentation.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form

@Composable
fun LoginLayout(content: @Composable() ()->Unit){
    Div({classes("flex min-h-full items-center justify-center py-12 px-4 sm:px-6 lg:px-8".split(" "))}) {
        Div({classes("w-full max-w-md space-y-8".split(" "))}) {
            Form(attrs = {classes("mt-8 space-y-6".split(" "))}) {
                content()
            }
        }
    }
}