package model.messages

import kotlinx.serialization.Serializable


@Serializable
sealed class WebsocketResponse

@Serializable
sealed class WebsocketRequest