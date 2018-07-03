data class Vector(val x: Float, val y: Float, val z: Float) {

    operator fun unaryMinus() = Vector(-x, -y, -z)

    operator fun times(s: Float) = Vector(x * s, y * s, z * s)

    operator fun Float.times(v: Vector) = Vector(this * v.x, this * v.y, this * v.z)

    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)

    operator fun minus(v: Vector) = Vector(x - v.x, y - v.y, z - v.z)

    infix fun x(v: Vector) = Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)

    fun dot(v: Vector) = x * v.x + y * v.y + z * v.z

    fun magnitude() = Maths.sqrt(x * x + y * y + z * z)

    fun normalize(): Vector {
        val m = magnitude()
        return Vector(x / m, y / m, z / m)
    }
}