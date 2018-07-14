package shapes

import Shape
import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray
import basics.Vector

class Rectangle(val width: Float, val height: Float) : Shape() {

    override val boundingBox: BoundingBox = BoundingBox(getLow(), getHigh())

    override fun intersect(ray: Ray): Hit? {
        if (ray.direction.z == 0.0f) return null
        val t = -(ray.origin.z / ray.direction.z)
        if (t < 0.0f) return null
        val x = ray.origin.x + t * ray.direction.x
        val y = ray.origin.y + t * ray.direction.y
        if (x <= 0.0f || x >= width || y <= 0.0f || y >= height) return null
        return Hit(t, Vector(0.0f, 0.0f, 1.0f))
    }

    private fun getLow(): Point {
        return Point(0.0f, 0.0f, 0.0f)
    }

    private fun getHigh(): Point {
        return Point(width, height, 0.0f)
    }

    override fun inside(point: Point): Boolean {
        return point.x <= 0.0f || point.x >= width || point.y <= 0.0f || point.y >= height && point.z == 0.0f
    }
}