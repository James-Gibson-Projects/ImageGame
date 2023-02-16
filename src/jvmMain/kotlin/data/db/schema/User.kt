package data.db.schema

import domain.model.User
import uk.gibby.neo4k.returns.graph.entities.Node
import uk.gibby.neo4k.returns.primitives.StringReturn
import uk.gibby.neo4k.returns.util.ReturnScope

data class UserNode(
    val username: StringReturn,
    val passwordHash: StringReturn
): Node<User>(){
    override fun ReturnScope.decode() = User(
        ::username.result(),
        ::passwordHash.result(),
    )
}
