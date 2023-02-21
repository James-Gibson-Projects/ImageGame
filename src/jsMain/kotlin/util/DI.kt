package util

import data.*
import data.repo.*
import data.websocket.WebSocket
import data.websocket.WebSocketImpl
import defaultClient
import org.koin.dsl.module

@Suppress("unused")
private val lazy = LazyThreadSafetyMode.PUBLICATION

val appModule = module {
    single<WebSocket>{ WebSocketImpl(defaultClient) }
    single<LoginRepo> { LoginRepoImpl() }
    single<FriendClientRepo> { FriendClientRepoImpl(get()) }
}