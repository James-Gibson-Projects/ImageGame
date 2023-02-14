package data.db

import uk.gibby.redis.core.RedisGraph

class Neo4jDatabaseImpl : Neo4jDatabase {
    override val graph: RedisGraph
        get() = TODO("Not yet implemented")
}