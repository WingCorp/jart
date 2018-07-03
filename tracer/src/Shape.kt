interface Shape{

    fun intersect(ray: Ray): Hit?
    fun boundingBox()
    fun inside(point: Point)
}