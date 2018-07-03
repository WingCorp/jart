object Maths {

    fun pow(a: Float, b: Float): Float {
        return Math.pow(a.toDouble(), b.toDouble()).toFloat()
    }

    fun sqrt(a: Float): Float {
        return Math.sqrt(a.toDouble()).toFloat()
    }
}