package lights

import basics.Colour
import Light
import basics.Point
import basics.Vector

data class PointLight(
        val point: Point,
        override val baseColour: Colour,
        override val intensity: Float
) : Light {
    override val geometricFactor: Float
        get() = 1f
    override val probabilityDensity: Float
        get() = 1f

    override fun direction(p: Point): Vector {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shadow(p: Point): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}