package data.repo
import data.db.Database
import data.db.schema.UserSession
import data.db.schema.createSession
import data.db.schema.updateSession
import domain.repo.FriendWebsocketRepo
import domain.repo.UserRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mindrot.jbcrypt.BCrypt
import uk.gibby.redis.conditions.equality.eq
import uk.gibby.redis.core.invoke
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.generated.UserSessionNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.results.get
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.statements.Delete.Companion.delete
import uk.gibby.redis.statements.Match.Companion.match
import uk.gibby.redis.statements.OrderBy.Companion.orderByDesc
import uk.gibby.redis.statements.Skip.Companion.skip
import uk.gibby.redis.statements.Where.Companion.where
import uk.gibby.redis.statements.WithAs.Companion.using

class UserRepoImpl(database: Database): UserRepo, KoinComponent {
    val websocketRepo: FriendWebsocketRepo by inject()
    private val graph = database.graph
    override fun createUser(name: String, pass: String): UserSession? {
        return if(graph.query { match(::UserNode{ it[username] = name }) }.isEmpty()){
            graph.query {
                val userNode = create(::UserNode{ it[username] = name; it[passwordHash] = hash(pass) })
                val userRef = using(userNode)
                val session = createSession(userRef)
                session
            }.first()
        } else null
    }
    override suspend fun loginUser(name: String, password: String): UserSession? {
        val hash = graph.query {
            match(::UserNode{ it[username] = name }).passwordHash
        }.firstOrNull()
        return hash?.let {
            if(!BCrypt.checkpw(password, it)) return null
            graph.query {
                val user = match(::UserNode{ it[username] = name })
                val (_, _, oldSession) = match(user - { authorisedBy } - ::UserSessionNode)
                delete(oldSession)
            }
            graph.query { createSession(using(match(::UserNode{ it[username] = name }))) }
        }?.firstOrNull()
            .also {
                if(it != null){
                    graph.query {
                        val (_, otherUser, otherUserSession) = match(::UserNode{it[username] = name} - {friendsWith} - ::UserNode - {authorisedBy} - ::UserSessionNode).nodes()
                        where(!(otherUserSession.key eq "IN_ACTIVE"))
                        otherUser.username
                    }.forEach { websocketRepo.updateUser(it) }
                }
            }
    }
    override fun logoutUser(session: UserSession){
        graph.query {
            delete(match(::UserSessionNode[session]))
        }
    }
    override fun verifySession(session: UserSession) = graph.query {
        val sessionNode = match(::UserSessionNode{
            it[username] = session.username
            it[key] = session.key
        })
        where(!(sessionNode.key eq "IN_ACTIVE"))
        updateSession(sessionNode)
    }.firstOrNull()
    private fun deleteOldSessions(username: String) = graph.query {
        val session = match(::UserSessionNode{it[this.username] = username})
        orderByDesc(session.lastActive)
        skip(1)
        delete(session)

    }
    companion object {
        @JvmStatic
        private fun hash(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())
    }
}