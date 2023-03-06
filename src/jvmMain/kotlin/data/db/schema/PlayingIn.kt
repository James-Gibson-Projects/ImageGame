package data.db.schema

import uk.gibby.neo4k.returns.graph.entities.UnitDirectionalRelationship
import uk.gibby.neo4k.returns.primitives.BooleanReturn

class PlayingIn(val playingAsWhite: BooleanReturn): UnitDirectionalRelationship<UserNode, Game>()