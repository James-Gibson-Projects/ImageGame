package data.repo

import data.db.Neo4jDatabase
import data.db.schema.AuthenticatedBy
import data.db.schema.UserNode
import data.db.schema.UserSessionNode
import domain.model.UserSession
import domain.repo.UserRepo
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt
import uk.gibby.neo4k.clauses.Create.Companion.create
import uk.gibby.neo4k.clauses.DetachDelete.Companion.detachDelete
import uk.gibby.neo4k.clauses.Match.Companion.match
import uk.gibby.neo4k.core.Graph
import uk.gibby.neo4k.core.invoke
import uk.gibby.neo4k.paths.`o-→`
import uk.gibby.neo4k.paths.`←-o`
import uk.gibby.neo4k.queries.build
import uk.gibby.neo4k.queries.query
import uk.gibby.neo4k.returns.primitives.StringReturn

class UserRepoImpl : UserRepo, KoinComponent{
    val graph: Graph
    init {
        val db by inject<Neo4jDatabase>()
        graph = db.graph
    }
    override fun createUser(name: String, pass: String): UserSession? {
        val salt = BCrypt.gensalt()
        val passwordHash = BCrypt.hashpw(pass, salt)
        val user = graph.createUser(name, passwordHash).first()
        return graph.createSession(user.username, generateSessionId()).firstOrNull()
    }

    override suspend fun loginUser(name: String, password: String): UserSession? {
        return graph.findUser(name, password).firstOrNull()?.let {
            graph.createSession(it.username, generateSessionId()).first()
        }
    }

    override fun logoutUser(session: UserSession) {
        graph.logoutUser(session.username)
    }

    override fun verifySession(session: UserSession): UserSession? {
        return graph.findSession(session.username, session.key).firstOrNull()
    }


    companion object{
        val createUser = query(::StringReturn, ::StringReturn) { name, passHash ->
            create(::UserNode{ it[username] = name; it[passwordHash] = passHash })
        }.build()

        val findUser = query(::StringReturn, ::StringReturn) { name, passHash ->
            match(::UserNode{ it[username] = name; it[passwordHash] = passHash })
        }.build()

        val createSession = query(::StringReturn, ::StringReturn) { name, sessionId->
            val user = match(::UserNode{ it[username] = name})
            val (_, _, session) = create(user `o-→` ::AuthenticatedBy `o-→` ::UserSessionNode{ it[key] = sessionId; it[name] = user.username })
            session
        }.build()

        val logoutUser = query(::StringReturn){ name ->
            val (session) = match(::UserSessionNode `←-o` ::AuthenticatedBy `←-o` ::UserNode{ it[username] = name })
            detachDelete(session)
        }.build()

        val findSession = query (::StringReturn, ::StringReturn){ name, key ->
            match(::UserSessionNode{it[username] = name; it[this.key] = key})
        }.build()
    }
}