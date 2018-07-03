data class Colour(val r: Float, val g: Float, val b: Float) {
    init {
        r = norm(r)
        g = norm(g)
        b = norm(b)
    }



    private fun norm(v: Float) : Float {
        return when {
            v <= 0.0f -> 0.0f
            v >= 1.0f -> 1.0f
            else -> v
        }
    }
}