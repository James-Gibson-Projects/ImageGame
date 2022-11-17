package util

import data.db.Database
import data.repo.*
import net.malkowscy.application.data.db.DatabaseImpl
import domain.repo.FriendRequestRepo
import domain.repo.FriendRepo
import domain.repo.FriendWebsocketRepo
import net.malkowscy.application.domain.repo.InviteRepo
import domain.repo.UserRepo
import org.koin.dsl.module

val defaultModule = module {
    single<Database> { DatabaseImpl() }
    single<UserRepo> { UserRepoImpl(database = get()) }
    single<InviteRepo>{ InviteRepoImpl() }
    single<FriendRequestRepo>{ FriendRequestRepoImpl() }
    single<FriendWebsocketRepo>{ FriendWebsocketRepoImpl() }
    single<FriendRepo>{ FriendRepoImpl() }
}