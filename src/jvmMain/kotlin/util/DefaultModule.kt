package util

import data.repo.FriendClientRepo
import data.repo.FriendClientRepoImpl
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
    single<FriendRequestRepo>{ FriendRequestRepoImpl() }
    single<FriendRepo>{ FriendRepoImpl() }

    single<FriendClientRepo> { FriendClientRepoImpl(get()) }
    single<FriendRequestHandler> { FriendRequestHandlerImpl() }
    single<LoginRepo> { LoginRepoImpl() }
}