package data.db.schema

import uk.gibby.neo4k.returns.graph.entities.UnitDirectionalRelationship

class SentFriendRequestTo: UnitDirectionalRelationship<UserNode, UserNode>()