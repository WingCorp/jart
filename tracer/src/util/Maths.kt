package util

object Maths {

    fun e(): Float {
        return 0.00001f
    }

    fun pow(a: Float, b: Float): Float {
        return Math.pow(a.toDouble(), b.toDouble()).toFloat()
    }

    fun sqrt(a: Float): Float {
        return Math.sqrt(a.toDouble()).toFloat()
    }
}