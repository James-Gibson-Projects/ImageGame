package data.repo

import domain.repo.FriendRequestRepo
import data.db.Database
import domain.exceptions.InviteAlreadySentException
import domain.exceptions.SelfInviteException
import domain.exceptions.UserNotFoundException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.redis.core.invoke
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.statements.Delete.Companion.delete

class FriendRequestRepoImpl: FriendRequestRepo, KoinComponent {
    private val db by inject<Database>()
    override fun sendRequest(fromUsername: String, toUsername: String) {
        if(fromUsername == toUsername) throw SelfInviteException()
        if(db.graph.query { match(::UserNode{ it[username] = toUsername }, ::UserNode{ it[username] = fromUsername }).first }.isEmpty()) throw UserNotFoundException()
        val inviteAlreadySent = db.graph.query {
            val (_, invite, _) = match((::UserNode){ it[username] = fromUsername } - { sentFriendRequestTo } - (::UserNode){ it[username] = toUsername })
            invite
        }.isNotEmpty()
        if(inviteAlreadySent) throw InviteAlreadySentException()
        val alreadyInvited = db.graph.query {
            val (_, invite, _) = match((::UserNode){ it[username] = toUsername } - { sentFriendRequestTo } - (::UserNode){ it[username] = fromUsername })
            invite
        }.isNotEmpty()
        if (alreadyInvited){
            db.graph.query {
                val (to, invite, from) = match((::UserNode){ it[username] = toUsername } - { sentFriendRequestTo } - (::UserNode){ it[username] = fromUsername })
                create(to - {friendsWith} - from)
                create(from - {friendsWith} - to)
                delete(invite)
            }
        } else {
            db.graph.query {
                val (from, to) = match(
                    (::UserNode){ it[username] = fromUsername },
                    (::UserNode){ it[username] = toUsername }
                )
                create(from - { sentFriendRequestTo } - to)
            }
        }
    }

    override fun getUserIncomingInvites(name: String) = db.graph.query {
        match(::UserNode - { sentFriendRequestTo } - ::UserNode{ it[username] = name }).first
    }.map { it.username }

    override fun getUserOutgoingInvites(name: String) = db.graph.query {
        match((::UserNode){ it[username] = name } - { sentFriendRequestTo } - ::UserNode).second
    }.map { it.username }
}