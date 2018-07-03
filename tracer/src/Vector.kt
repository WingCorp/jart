data class Vector(val x: Float, val y: Float, val z: Float) {

    operator fun unaryMinus() = Vector(-x, -y, -z)
    operator fun times(s: Float) = Vector(x * s, y * s, z * s)
    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
    operator fun minus(v: Vector) = Vector(x - v.x, y - v.y, z - v.z)

    infix fun x(v: Vector) = Vector(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)

    fun dotProduct(v: Vector) = x * v.x + y * v.y + z * v.z
    fun magnitude() = (Math.sqrt(((x * x) + (y * y) + (z * z)).toDouble())).toFloat()
    fun normalize(): Vector {
        val m = magnitude()
        return Vector(x / m, y / m, z / m)
    }
}