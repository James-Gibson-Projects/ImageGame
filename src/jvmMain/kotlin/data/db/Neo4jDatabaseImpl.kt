package data.db

import util.Config
import util.config
import uk.gibby.neo4k.core.Graph

class Neo4jDatabaseImpl : Neo4jDatabase {

    override val graph = Graph(
        name = "neo4j",
        host = config[Config.dbHost],
        username = config[Config.dbUser],
        password = config[Config.dbPass]
    ).also { it.create() }
}