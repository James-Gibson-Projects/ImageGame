package data.repo

import data.db.schema.SentFriendRequestTo
import data.db.schema.UserNode
import domain.repo.InviteRepo
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

class InviteRepoImpl : InviteRepo {
    override fun sendInvite(fromUsername: String, toUsername: String) {
        TODO("Not yet implemented")
    }

    override fun getUserIncomingInvites(name: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun getUserOutgoingInvites(name: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun acceptInvite(fromUsername: String, toUsername: String) {
        TODO("Not yet implemented")
    }
}