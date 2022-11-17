package data.db.schema

import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.RedisType
import uk.gibby.redis.annotation.Relates

@RedisType
class EmptyData
@Node
@Relates(to = UserSession::class, by = "authorisedBy", data = EmptyData::class)
@Relates(to = User::class, by = "invited", data = EmptyData::class)
@Relates(to = Game::class, by = "inGame", data = InGameRelationData::class)
@Relates(to = User::class, by = "friendsWith", data = EmptyData::class)
@Relates(to = User::class, by = "sentFriendRequestTo", data = EmptyData::class)
data class User(
    val username: String,
    val passwordHash: String
)

@RedisType
data class InGameRelationData(val color: Piece.Color)