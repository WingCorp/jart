package lights

import basics.Colour
import Light
import basics.Point
import basics.Vector

data class DirectionalLight(
        val direction: Vector,
        override val intensity: Float,
        override val baseColour: Colour,
        override val geometricFactor: Float,
        override val probabilityDensity: Float
) : Light {
    override fun direction(p: Point): Vector {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shadow(p: Point): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}