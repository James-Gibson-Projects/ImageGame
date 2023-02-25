package data.db.schema

import uk.gibby.neo4k.returns.graph.entities.UnitDirectionalRelationship

class SentGameRequestTo: UnitDirectionalRelationship<UserNode, UserNode>()