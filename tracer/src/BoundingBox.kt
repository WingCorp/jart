class BoundingBox(val lowPoint: Point, val highPoint: Point) {

    fun intersect(ray: Ray) : Boolean{
        val tx: Float
        val ty: Float
        val tz: Float
        val dtx: Float
        val dty: Float
        val dtz: Float

        if(ray.direction.x >= 0.0f){
            tx = (lowPoint.x - ray.point.x)/ray.direction.x
            dtx = (highPoint.x - ray.point.x)/ray.direction.x
        } else {
            tx = (highPoint.x - ray.point.x)/ray.direction.x
            dtx = (lowPoint.x - ray.point.x)/ray.direction.x
        }

        if(ray.direction.y >= 0.0f){
            ty = (lowPoint.y - ray.point.y)/ray.direction.y
            dty = (highPoint.y - ray.point.y)/ray.direction.y
        } else {
            ty = (highPoint.y - ray.point.y)/ray.direction.y
            dty = (lowPoint.y - ray.point.y)/ray.direction.y
        }

        if(ray.direction.z >= 0.0f){
            tz = (lowPoint.z - ray.point.z)/ray.direction.z
            dtz = (highPoint.z - ray.point.z)/ray.direction.z
        } else {
            tz = (highPoint.z - ray.point.z)/ray.direction.z
            dtz = (lowPoint.z - ray.point.z)/ray.direction.z
        }

        val t = maxOf(tx, ty, tz)
        val dt = minOf(dtx, dty, dtz)

        return (t < dt && dt > 0.0f)
    }
}