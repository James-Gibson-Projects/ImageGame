package util

import data.db.Database
import data.db.Neo4jDatabase
import data.db.Neo4jDatabaseImpl
import data.repo.*
import domain.repo.*
import net.malkowscy.application.data.db.DatabaseImpl
import org.koin.dsl.module

val defaultModule = module {
    single<Neo4jDatabase> { Neo4jDatabaseImpl() }
    single<UserRepo> { UserRepoImpl() }
    single<InviteRepo>{ InviteRepoImpl() }
    single<FriendRequestRepo>{ FriendRequestRepoImpl() }
    single<FriendWebsocketRepo>{ FriendWebsocketRepoImpl() }
    single<FriendRepo>{ FriendRepoImpl() }
}