package shapes

import Shape
import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray

class Triangle(val a: Point, val b: Point, val c: Point) : Shape() {

    override val boundingBox = BoundingBox(getLow(), getHigh())

    override fun intersect(ray: Ray): Hit? {
        val a = this.a.x - this.b.x
        val b = this.a.x - this.c.x
        val c = ray.direction.x
        val d = this.a.x - ray.origin.x
        val e = this.a.y - this.b.y
        val f = this.a.y - this.c.y
        val g = ray.direction.y
        val h = this.a.y - ray.origin.y
        val i = this.a.z - this.b.z
        val j = this.a.z - this.c.z
        val k = ray.direction.z
        val l = this.a.z - ray.origin.z

        val D = a * (f * k - g * j) + b * (g * i - e * k) + c * (e * j - f * i)

        if (D == 0.0f) {
            return null
        }

        val beta = (d * (f * k - g * k) + b * (g * l - h * k) + c * (h * j - f * l)) / D
        val gamma = (a * (h * k - g * l) + d * (g * i - e * k) + c * (e * l - h * i)) / D
        val t = (a * (f * l - h * j) + b * (h * i - e * l) + d * (e * j - f * i))

        return if (beta > 0.0f && beta < 1.0f && gamma > 0.0f && gamma < 1.0f && beta + gamma > 0.0f && beta + gamma < 1.0f && t > 0.0f) {
            val normal = (this.a.distance(this.b) x this.a.distance(this.c)).normalize()
            Hit(t, normal)
        } else {
            null
        }
    }

    private fun getLow(): Point {
        val x = minOf(a.x, b.x, c.x)
        val y = minOf(a.y, b.y, c.y)
        val z = minOf(a.z, b.z, c.z)
        return Point(x, y, z)
    }

    private fun getHigh(): Point {
        val x = maxOf(a.x, b.x, c.x)
        val y = maxOf(a.y, b.y, c.y)
        val z = maxOf(a.z, b.z, c.z)
        return Point(x, y, z)
    }

    override fun inside(point: Point): Boolean {
        return false
    }

}