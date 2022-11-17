package data.db.schema

import kotlinx.serialization.Serializable
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.generated.UserLastOnlineResult
import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.StringResult


@RedisType
data class UserLastOnline(val username: String, val lastOnline: Long)

fun userLastOnline(username: StringResult, lastOnline: LongResult): UserLastOnlineResult{
    return object: UserLastOnlineResult(){
        override val lastOnline = lastOnline
        override val username = username
    }
}