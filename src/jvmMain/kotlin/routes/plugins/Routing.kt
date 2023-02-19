
package routes.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.*
import routes.http.configureUserRoutes
import routes.web_sockets.configureInviteRoutes

fun Application.configureRouting() {
	configureUserRoutes()
	configureInviteRoutes()
	routing {
		get("/webapp/{...}") {
			call.respondHtml {
				head { title("Chess") }
				body {
					div { id = "root" }
					script { src = "/static/js.js" }
				}
			}
		}
		// Static plugin. Try to access `/static/index.html`
		static("/static") {
			resources()
		}
	}
}
