package data.db.schema

import uk.gibby.neo4k.returns.graph.entities.UnitDirectionalRelationship

class AuthenticatedBy: UnitDirectionalRelationship<UserNode, UserSessionNode>()