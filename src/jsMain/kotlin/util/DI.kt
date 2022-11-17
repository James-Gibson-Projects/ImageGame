package util

import data.database.Remote
import data.database.RemoteImpl
import data.repo.FriendRequestRepoImpl
import data.repo.FriendsRepoImpl
import data.repo.InviteRepoImpl
import data.repo.UserRepoImpl
import domain.repos.FriendRequestRepo
import domain.repos.FriendsRepo
import domain.repos.InviteRepo
import domain.repos.UserRepo
import org.koin.dsl.module

@Suppress("unused")
private val lazy = LazyThreadSafetyMode.PUBLICATION

val appModule = module {
    single<Remote> { RemoteImpl() }
    single<UserRepo> { UserRepoImpl() }
    single<InviteRepo> { InviteRepoImpl() }
    single<FriendsRepo> { FriendsRepoImpl() }
    single<FriendRequestRepo> { FriendRequestRepoImpl() }
}