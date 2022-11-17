package net.malkowscy.application.util

import com.natpryce.konfig.*

object Config {
    private val config = ConfigurationProperties.fromResource("local.properties")
    val server_port = Key("server.port", intType)
    val server_host = Key("server.host", stringType)
    val server_pass = Key("server.pass", stringType)
}
val config = ConfigurationProperties.fromResource("local.properties")