package data.repo

import domain.repo.FriendRepo
import data.db.Database
import data.db.schema.UserLastOnline
import data.db.schema.userLastOnline
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.core.invoke
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.generated.UserSessionNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.statements.Delete.Companion.delete

class FriendRepoImpl: FriendRepo, KoinComponent {
    val db by inject<Database>()
    override fun getFriends(username: String): List<UserLastOnline> = db.graph.query {
        val (_, friend, session) = match(::UserNode{it[this.username] = username} - {friendsWith} - ::UserNode - {authorisedBy} - ::UserSessionNode).nodes()
        userLastOnline(friend.username, session.lastActive)
    }

    override fun deleteFriendship(firstUsername: String, secondUsername: String) {
        db.graph.query {
            val (_, outgoing) = match(::UserNode{ it[username] = firstUsername } - { friendsWith } - ::UserNode{ it[username] = secondUsername })
            delete(outgoing)
            val (_, incoming) = match(::UserNode{ it[username] = firstUsername } - { friendsWith } - ::UserNode{ it[username] = secondUsername })
            delete(incoming)
        }
    }

}