package data.db.schema

import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import uk.gibby.redis.annotation.Node
import uk.gibby.redis.generated.UserNode
import uk.gibby.redis.generated.UserSessionNode
import uk.gibby.redis.paths.minus
import uk.gibby.redis.scopes.QueryScope
import uk.gibby.redis.statements.Create.Companion.create
import uk.gibby.redis.core.invoke
import uk.gibby.redis.core.toValue
import uk.gibby.redis.statements.Update.Companion.set
import java.util.Date

@Node
data class UserSession(val username: String, val key: String, val lastActive: Long): Principal

fun QueryScope.createSession(user: UserNode): UserSessionNode{
    val newSession = ::UserSessionNode{
        it[username] = user.username
        it[key] = generateSessionId()
        it[lastActive] = Date().time
    }
    return create(user - {authorisedBy} - newSession).second
}

fun QueryScope.updateSession(session: UserSessionNode): UserSessionNode{
    set(session.lastActive toValue Date().time)
    return session
}



