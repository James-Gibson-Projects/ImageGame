package data.db.schema

import domain.model.UserSession
import uk.gibby.neo4k.returns.graph.entities.Node
import uk.gibby.neo4k.returns.primitives.StringReturn
import uk.gibby.neo4k.returns.util.ReturnScope

data class UserSessionNode(
    val username: StringReturn,
    val key: StringReturn,
): Node<UserSession>() {
    override fun ReturnScope.decode() = UserSession(
        ::username.result(),
        ::key.result(),
    )
}
