import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray

abstract class Shape {

    abstract val boundingBox: BoundingBox

    abstract fun intersect(ray: Ray): Hit?

    abstract fun inside(point: Point): Boolean

}