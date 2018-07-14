package shapes

import Shape
import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray
import basics.Vector
import util.Maths

class Disk(val radius: Float) : Shape() {

    override val boundingBox: BoundingBox = BoundingBox(getLow(), getHigh())

    override fun intersect(ray: Ray): Hit? {
        if (ray.direction.z == 0.0f) return null
        val t = -(ray.origin.z / ray.direction.z)
        if (t < 0.0f) return null
        val x = ray.origin.x + t * ray.direction.x
        val y = ray.origin.y + t * ray.direction.y
        if ((Maths.pow(x, 2.0f) + Maths.pow(y, 2.0f)) > Maths.pow(radius, 2.0f)) return null
        return Hit(t, Vector(0.0f, 0.0f, 1.0f))
    }
    private fun getLow() : Point {
        return Point(-radius, -radius, 0.0f)
    }

    private fun getHigh() : Point {
        return Point(radius, radius, 0.0f)
    }

    override fun inside(point: Point): Boolean {
        return (Maths.pow(point.x, 2.0f) + Maths.pow(point.y, 2.0f)) > Maths.pow(radius, 2.0f) && point.z == 0.0f
    }
}