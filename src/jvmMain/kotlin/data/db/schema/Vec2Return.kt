package data.db.schema

import model.messages.Vec2
import uk.gibby.neo4k.returns.generic.StructReturn
import uk.gibby.neo4k.returns.primitives.LongReturn
import uk.gibby.neo4k.returns.util.ReturnScope

class Vec2Return(val x: LongReturn, val y: LongReturn): StructReturn<Vec2>() {
    override fun ReturnScope.decode() = Vec2(::x.result().toInt(), ::y.result().toInt())

    override fun StructParamMap.encodeStruct(value: Vec2) {
        x[value.x.toLong()]
        y[value.y.toLong()]
    }
}