import org.jetbrains.compose.web.renderComposable
import org.koin.core.context.startKoin
import presentation.AppRoot
import util.appModule


fun main() {
    kotlinext.js.require("./app.css")
    startKoin { modules(appModule) }
    val app = AppRoot()
    renderComposable("root"){ app.Root() }
}

