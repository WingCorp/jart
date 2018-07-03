import kotlin.math.min

class Sphere(val center: Point, val radius: Float) : Shape {
    
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
                    val normal = Vector(center.x/radius, center.y/radius, center.z/radius).normalize()
                    Hit(t, normal)
                }
            }
        }
    }

    fun getLow(){
        val p = Point((0.0f - radius) + Maths.e(), (0.0f))
    }
    override fun boundingBox() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun inside(point: Point){
        return when{

        }
    }
}