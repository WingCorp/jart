abstract class FloatTriplet(x: Float, y: Float, z: Float) {

    private val values: FloatArray = floatArrayOf(x, y, z)

    operator fun get(index: Int) = values[index]

    abstract fun immutableSet(index: Int, value: Float): FloatTriplet
}