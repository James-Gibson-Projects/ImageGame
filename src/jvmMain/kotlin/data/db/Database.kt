package data.db

import uk.gibby.redis.core.RedisGraph

interface Database {
    val graph: RedisGraph
}