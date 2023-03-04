package presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.ExperimentalComposeWebSvgApi
import org.jetbrains.compose.web.attributes.forId

import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.svg.*


@OptIn(ExperimentalComposeWebSvgApi::class)
@Composable
fun DefaultButton(
    text: String,
    color: String = "indigo",
    action: () -> Unit,
){
    Button({
        classes(("group relative flex w-full justify-center rounded-md" +
                " border border-transparent bg-$color-600 py-2 px-4 text-sm" +
                " font-medium text-white hover:bg-$color-700 focus:outline-none" +
                " focus:ring-2 focus:ring-$color-500 focus:ring-offset-2").split(" "))
        onClick{ action() }
    }) {
        Span(attrs = { classes("absolute inset-y-0 left-0 flex items-center pl-3".split(" ")) }) {
            Svg(viewBox = "0 0 20 20",
                attrs = {
                    xmlns("http://www.w3.org/2000/svg")
                    classes("h-5 w-5 text-$color-500 group-hover:text-$color-400".split(" "))
                    fill("currentColor")
                }
            ) {
                Path("M10 1a4.5 4.5 0 00-4.5 4.5V9H5a2 2 0 00-2 2v6a2 2 0 002 2h10a2 2 0 002-2v-6a2 2 0 00-2-2h-.5V5.5A4.5 4.5 0 0010 1zm3 8V5.5a3 3 0 10-6 0V9h6z",
                    attrs = { fillRule("evenodd") }
                )
            }
        }
        Text(text)
    }

}



