package util

import data.repo.FriendRequestClientRepo
import data.repo.FriendRequestClientRepoImpl
import data.db.Neo4jDatabase
import data.db.Neo4jDatabaseImpl
import data.repo.*
import domain.repo.*
import org.koin.dsl.module
import routes.web_sockets.FriendRequestHandler
import routes.web_sockets.FriendRequestHandlerImpl

val defaultModule = module {
    single<Neo4jDatabase> { Neo4jDatabaseImpl() }
    single<UserRepo> { UserRepoImpl() }
    single<InviteRepo>{ InviteRepoImpl() }
    single<FriendRequestRepo>{ FriendRequestRepoImpl() }
    single<FriendRepo>{ FriendRepoImpl() }
    single<FriendRequestClientRepo> { FriendRequestClientRepoImpl(get()) }
    single<FriendRequestHandler> { FriendRequestHandlerImpl() }
}