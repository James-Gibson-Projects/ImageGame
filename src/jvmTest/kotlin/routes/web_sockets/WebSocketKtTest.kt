package routes.web_sockets;

import applicationModule

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class WebSocketKtTest {

    @Test
    fun testWebsocketLive() = testApplication {
        application {
            applicationModule()
        }
        val client = createClient {
            install(WebSockets)
        }
        client.webSocket("/live") {
            TODO("Please write your test here")
        }
    }
}