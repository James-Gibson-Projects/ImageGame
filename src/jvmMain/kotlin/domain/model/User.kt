package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val passwordHash: String
)
