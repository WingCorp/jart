package shapes

import Shape
import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray

class OpenCylinder(val radius: Float, val height: Float) : Shape() {

    override val boundingBox: BoundingBox = BoundingBox(getLow(), getHigh())

    override fun intersect(ray: Ray): Hit? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getLow() : Point{
        return Point(-radius, -(height/2), -radius)
    }

    private fun getHigh() : Point{
        return Point(radius, height/2, radius)
    }

    override fun inside(point: Point): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}