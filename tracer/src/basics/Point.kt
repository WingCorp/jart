package basics

data class Point(val x: Float, val y: Float, val z: Float): FloatTriplet(x, y, z) {

    operator fun unaryMinus() = Point(-x, -y, -z)

    operator fun minus(p: Point) = Point(x - p.x, y - p.y, z - p.z)

    operator fun plus(p: Point) = Point(x + p.x, y + p.y, z + p.z)

    operator fun div(f: Float) = Point(x / f, y / f, z / f)

    operator fun times(f: Float) = Point(x * f, y * f, z * f)

    fun move(v: Vector) = Point(v.x + x, v.y + y, v.z + z)

    fun distance(p: Point) = Vector(p.x - x, p.y - y, p.z - z)

    fun direction(p: Point) = distance(p).normalize()

    override fun immutableSet(index: Int, value: Float): Point {
        return when (index) {
            0 -> Point(value, y, z)
            1 -> Point(x, value, z)
            2 -> Point(x, y, value)
            else -> error("Cannot create immutableSet with modified value at index: $index," +
                    "index must be within [0;2].")
        }
    }
}