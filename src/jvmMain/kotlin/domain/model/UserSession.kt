package domain.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val username: String,
    val key: String,
): Principal
