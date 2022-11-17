package data.repo

import data.db.Database
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import net.malkowscy.application.domain.repo.InviteRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.redis.core.invoke
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Match.Companion.match

class InviteRepoImpl: InviteRepo, KoinComponent {
    private val db by inject<Database>()
    override fun sendInvite(fromUsername: String, toUsername: String) {
        if(fromUsername == toUsername) throw SelfInviteException()
        if(db.graph.query { match(::UserNode{ it[username] = toUsername }) }.isEmpty()) throw UserNotFoundException()
        val inviteExists = db.graph.query {
            val (_, invite, _) = match(::UserNode{ it[username] = fromUsername } - { invited } - ::UserNode{ it[username] = toUsername })
            invite
        }.isNotEmpty()
        if(inviteExists) throw InviteAlreadySentException()
        db.graph.query {
            val (from, to) = match(
                ::UserNode{ it[username] = fromUsername },
                ::UserNode{ it[username] = toUsername }
            )
            create(from - { invited } - to)
        }
    }

    override fun getUserIncomingInvites(name: String) = db.graph.query {
        match(::UserNode - { invited } - ::UserNode{ it[username] = name }).first
    }.map { it.username }

    override fun getUserOutgoingInvites(name: String) = db.graph.query {
        match(::UserNode{ it[username] = name } - { invited } - ::UserNode).second
    }.map { it.username }
}