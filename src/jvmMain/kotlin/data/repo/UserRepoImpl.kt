package data.repo

import data.db.Neo4jDatabase
import domain.model.UserSession
import domain.repo.UserRepo
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt
import uk.gibby.neo4k.core.Graph
import uk.gibby.neo4k.core.invoke
import data.db.queries.*

class UserRepoImpl : UserRepo, KoinComponent{
    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun createUser(name: String, pass: String): UserSession? {
        if(graph.userExists(name).first()) return null
        val salt = BCrypt.gensalt()
        val passwordHash = BCrypt.hashpw(pass, salt)
        val user = graph.createUser(name, passwordHash).first()
        return graph.createSession(user.username, generateSessionId()).firstOrNull()
    }

    override suspend fun loginUser(name: String, password: String): UserSession? {
        val user = graph.findUser(name).firstOrNull() ?: return null
        return if(BCrypt.checkpw(password, user.passwordHash)){
            graph.logoutUser(name)
            graph.createSession(name, generateSessionId()).first()
        } else null
    }

    override fun logoutUser(session: UserSession) {
        graph.logoutUser(session.username)
    }

    override fun verifySession(session: UserSession): UserSession? {
        return graph.findSession(session.username, session.key).firstOrNull()
    }

}