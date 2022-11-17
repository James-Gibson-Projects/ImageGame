
package net.malkowscy.application.routes.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import routes.http.configureUserRoutes
import routes.web_sockets.configureInviteRoutes
import model.Message
import java.text.SimpleDateFormat
import java.util.*

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
		get("/api/msg") {
			val timestamp = SimpleDateFormat.getDateTimeInstance()
				.format(Date())
			call.respond(Message(timestamp, "Hello world from Ktor :-)"))
		}
		// Static plugin. Try to access `/static/index.html`
		static("/static") {
			resources()
		}
	}
}
