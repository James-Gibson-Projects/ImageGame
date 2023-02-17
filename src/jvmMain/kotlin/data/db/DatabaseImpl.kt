package net.malkowscy.application.data.db

import data.db.Database
import uk.gibby.redis.core.RedisGraph
import util.Config
import util.config

class DatabaseImpl: Database {
    override val graph = RedisGraph(
        name = "ChessServerGraph",
        host = config[Config.dbHost],
        port = config[Config.dbPort],
        password = config[Config.dbPass],
    )
}