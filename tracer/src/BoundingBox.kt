class BoundingBox(val low: Point, val high: Point) {

    fun hit(r: Ray) : Boolean{
        val tx: Float
        val ty: Float
        val tz: Float
        val dtx: Float
        val dty: Float
        val dtz: Float

        if(r.dir.x >= 0.0){
            tx = (low.x - r.o.x)/r.dir.x
            dtx = (high.x - r.o.x)/r.dir.x
        } else {
            tx = (high.x - r.o.x)/r.dir.x
            dtx = (low.x - r.o.x)/r.dir.x
        }

        if(r.dir.y >= 0.0){
            ty = (low.y - r.o.y)/r.dir.y
            dty = (high.y - r.o.y)/r.dir.y
        } else {
            ty = (high.y - r.o.y)/r.dir.y
            dty = (low.y - r.o.y)/r.dir.y
        }

        if(r.dir.z >= 0.0){
            tz = (low.z - r.o.z)/r.dir.z
            dtz = (high.z - r.o.z)/r.dir.z
        } else {
            tz = (high.z - r.o.z)/r.dir.z
            dtz = (low.z - r.o.z)/r.dir.z
        }

        val t = maxOf(tx, ty, tz)
        val dt = minOf(dtx, dty, dtz)

        return (t < dt && dt > 0.0)
    }
}