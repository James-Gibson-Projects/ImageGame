package util

import data.database.Remote
import data.database.RemoteImpl
import data.repo.InviteRepoImpl
import data.repo.UserRepoImpl
import domain.repos.InviteRepo
import domain.repos.UserRepo
import domain.use_cases.UserLoginUseCasesImpl
import org.koin.dsl.module
import presentation.use_cases.UserLoginUseCases

val appModule = module {
    single<Remote> { RemoteImpl() }
    single<UserRepo> { UserRepoImpl() }
    single<UserLoginUseCases> { UserLoginUseCasesImpl() }
    single<InviteRepo> { InviteRepoImpl() }
}