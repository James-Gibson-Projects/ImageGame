package data.db

import uk.gibby.neo4k.core.Graph

interface Neo4jDatabase {
    val graph: Graph
}