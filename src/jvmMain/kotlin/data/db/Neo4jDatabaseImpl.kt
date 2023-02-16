package data.db

import util.Config
import util.config
import uk.gibby.neo4k.core.Graph

class Neo4jDatabaseImpl : Neo4jDatabase {
    override val graph = Graph(
        name = "main",
        host = config[Config.server_host],
        username = config[Config.server_user],
        password = config[Config.server_pass]
    )
}