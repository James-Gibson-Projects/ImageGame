package domain.repo

import data.db.Neo4jDatabase
import data.db.schema.UserSession
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.Graph
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

class UserRepoImpl : UserRepo, KoinComponent{
    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun createUser(name: String, pass: String): UserSession? {

    }

    override suspend fun loginUser(name: String, password: String): UserSession? {
        TODO("Not yet implemented")
    }

    override fun logoutUser(session: UserSession) {
        TODO("Not yet implemented")
    }

    override fun verifySession(session: UserSession): UserSession? {
        TODO("Not yet implemented")
    }


    companion object{
        val createUser = query(::StringReturn, ::StringReturn) { username, password ->
            match()
        }
    }
}