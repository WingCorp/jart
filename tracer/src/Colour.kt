import Utilities.GAMMA_MY
import Utilities.GAMMA_Sdec
import Utilities.GAMMA_Senc

class Colour(r: Float, g: Float, b: Float) {

    val r: Float
    val g: Float
    val b: Float

    init {
        this.r = norm(r)
        this.g = norm(g)
        this.b = norm(b)
    }

    private fun norm(v: Float): Float = when {
        v <= 0.0f -> 0.0f
        v >= 1.0f -> 1.0f
        else -> v
    }

    fun merge(c: Colour, refl: Float): Colour {
        val refl = norm(refl)
        val reflP = 1.0f - norm(refl) //Reflection percentage
        val r = refl * r + reflP * c.r
        val g = refl * g + reflP * c.g
        val b = refl * b + reflP * c.b
        return Colour(r, g, b)
    }

    fun triple(): Triple<Float, Float, Float> = Triple(r, g, b)

    fun toPixel(): Int {
        val (pr, pg, pb) = encodeGamma(r, g, b)
        val (r, g, b) = Triple((pr * 255).toInt(), (pg * 255).toInt(), (pb * 255).toInt())
        val gb = (g.shl(8)) + b
        return (r.shl(16)) + gb
    }

    operator fun times(i: Float): Colour {
        return Colour(r*i, g*i, b*i)
    }

    companion object {

        fun decodeGamma(r: Float, g: Float, b: Float): Triple<Float, Float, Float> {
            val s = GAMMA_Sdec
            val my = GAMMA_MY
            val srmy = Maths.pow(s * r, my)
            val sgmy = Maths.pow(s * g, my)
            val sbmy = Maths.pow(s * b, my)
            return Triple(srmy, sgmy, sbmy)
        }

        fun encodeGamma(r: Float, g: Float, b: Float): Triple<Float, Float, Float> {
            val s = GAMMA_Senc
            return Triple(Maths.pow(r, s), Maths.pow(g, s), Maths.pow(b, s))
        }

        fun fromPixel(rgb: Int): Colour {
            val alphaPos = 0xFF
            val r = rgb.shr(16).and(alphaPos)
            val g = rgb.shr(8).and(alphaPos)
            val b = rgb.and(alphaPos)
            val decoded = decodeGamma(r / 255.0f, g / 255.0f, b / 250.0f)
            return Colour(decoded.first, decoded.second, decoded.third)
        }

        val BLACK = Colour(0.0f, 0.0f, 0.0f)
        val WHITE = Colour(1.0f, 1.0f, 1.0f)
    }
}