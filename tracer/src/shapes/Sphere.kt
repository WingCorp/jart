package shapes

import basics.BoundingBox
import basics.Hit
import util.Maths
import basics.Point
import basics.Ray
import Shape
import basics.Vector
import kotlin.math.min

class Sphere(val center: Point, val radius: Float) : Shape() {

    override val boundingBox = BoundingBox(getLow(), getHigh())

    private fun getCorrectSolution(t1: Float, t2: Float): Float? {
        return when {
            t1 > 0.0 && t2 < 0.0 -> t1
            t1 < 0.0 && t2 > 0.0 -> t2
            t1 > 0.0 && t2 > 0.0 -> min(t1, t2)
            else -> null
        }
    }

    override fun intersect(ray: Ray): Hit? {
        val a = (ray.direction.x * ray.direction.x) + (ray.direction.y * ray.direction.y) + (ray.direction.z * ray.direction.z)
        val b = 2.0f * (ray.origin.x * ray.direction.x + ray.origin.y * ray.direction.y + ray.origin.z * ray.direction.z)
        val c = (ray.origin.x * ray.origin.x) + (ray.origin.y * ray.origin.y) + (ray.origin.z * ray.origin.z) - (radius * radius)
        val d = (b * b) - (4 * a * c)

        return when {
            d < 0.0f -> null
            d == 0.0f -> {
                val t = -b / (2.0f * a)
                if (t > 0.0) {
                    val normal = Vector(center.x / radius, center.y / radius, center.z / radius).normalize()
                    Hit(t, normal)
                } else {
                    null
                }
            }
            else -> {
                val t1 = (-b + Maths.sqrt(d)) / (2.0f * a)
                val t2 = (-b - Maths.sqrt(d)) / (2.0f * a)

                val t = getCorrectSolution(t1,t2)

                if(t == null){
                    null
                } else {
                    val normal = Vector(center.x / radius, center.y / radius, center.z / radius).normalize()
                    Hit(t, normal)
                }
            }
        }
    }

    private fun getLow() = Point((0.0f - radius) - Maths.e(), (0.0f - radius) - Maths.e(), (0.0f - radius) - Maths.e())

    private fun getHigh() = Point((0.0f + radius) + Maths.e(), (0.0f + radius) + Maths.e(), (0.0f + radius) + Maths.e())

    override fun inside(point: Point) : Boolean {
        return (Maths.pow(point.x - center.x, 2.0f) + Maths.pow(point.y - center.y, 2.0f) + Maths.pow(point.z - center.z, 2.0f)) > Maths.pow(radius, 2.0f)
    }
}