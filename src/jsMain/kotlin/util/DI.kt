package util

import data.*
import data.remote.Remote
import data.remote.RemoteImpl
import data.repo.*
import data.websocket.WebSocket
import data.websocket.WebSocketImpl
import domain.repos.FriendsRepo
import domain.repos.InviteRepo
import org.koin.dsl.module

@Suppress("unused")
private val lazy = LazyThreadSafetyMode.PUBLICATION

val appModule = module {
    single<Remote> { RemoteImpl() }
    single<WebSocket>{ WebSocketImpl(defaultClient) }
    single<UserRepo> { UserRepoImpl() }
    single<InviteRepo> { InviteRepoImpl() }
    single<FriendsRepo> { FriendsRepoImpl() }
    single<FriendRequestClientRepo> { FriendRequestClientRepoImpl(get()) }
}