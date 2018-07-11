data class Point(val x: Float, val y: Float, val z: Float) {

    operator fun unaryMinus() = Point(-x, -y, -z)

    operator fun minus(p: Point) = Point(x - p.x, y - p.y, z - p.z)

    operator fun plus(p: Point) = Point(x + p.x, y + p.y, z + p.z)

    operator fun div(f: Float) = Point(x / f, y / f, z / f)

    fun move(v: Vector) = Point(v.x + x, v.y + y, v.z + z)

    fun distance(p: Point) = Vector(p.x - x, p.y - y, p.z - z)

    fun direction(p: Point) = distance(p).normalize()

}