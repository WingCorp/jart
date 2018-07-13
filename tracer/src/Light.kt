import basics.Colour
import basics.Point
import basics.Vector

interface Light {
    val intensity: Float
    val baseColour: Colour
    val colour: Colour
        get() = baseColour * intensity

    fun direction(p: Point): Vector
    fun shadow(p: Point): Boolean
    val geometricFactor: Float
    val probabilityDensity: Float
}