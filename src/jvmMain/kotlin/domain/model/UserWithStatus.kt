package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserWithStatus(val username: String, val isOnline: Boolean)
