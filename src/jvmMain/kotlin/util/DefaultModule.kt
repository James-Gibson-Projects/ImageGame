package util

import data.repo.FriendClientRepo
import data.repo.FriendClientRepoImpl
import data.db.Neo4jDatabase
import data.db.Neo4jDatabaseImpl
import data.repo.*
import domain.repo.*
import domain.repo.GameRequestRepo
import org.koin.dsl.module
import routes.web_sockets.*

val defaultModule = module {

    single<Neo4jDatabase> { Neo4jDatabaseImpl() }

    single<UserRepo> { UserRepoImpl() }
    single<FriendRequestRepo>{ FriendRequestRepoImpl() }
    single<FriendRepo>{ FriendRepoImpl() }
    single<GameRequestRepo>{ GameRequestRepoImpl() }
    single<GameRepo>{ GameRepoImpl() }

    single<FriendClientRepo> { FriendClientRepoImpl(get()) }
    single<FriendRequestHandler> { FriendRequestHandlerImpl() }
    single<GameInviteRequestHandler> { GameInviteRequestHandlerImpl() }
    single<GameRequestHandler> { GameRequestHandlerImpl() }
    single<LoginRepo> { LoginRepoImpl() }
}