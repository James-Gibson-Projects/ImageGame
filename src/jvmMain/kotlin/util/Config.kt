package util

import com.natpryce.konfig.*

object Config {
    val dbPort = Key("db.port", intType)
    val dbHost = Key("db.host", stringType)
    val dbUser = Key("db.user", stringType)
    val dbPass = Key("db.pass", stringType)
    val serverHost = Key("server.host", stringType)
}
val config = ConfigurationProperties.fromResource("local.properties")