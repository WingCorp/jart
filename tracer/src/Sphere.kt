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
        val b = 2.0f * (ray.point.x * ray.direction.x + ray.point.y * ray.direction.y + ray.point.z * ray.direction.z)
        val c = (ray.point.x * ray.point.x) + (ray.point.y * ray.point.y) + (ray.point.z * ray.point.z) - (radius * radius)
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
                val t1 = (-b + (Math.sqrt(d.toDouble())).toFloat()) / (2.0f * a)
                val t2 = (-b - (Math.sqrt(d.toDouble())).toFloat()) / (2.0f * a)

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

    override fun inside(point: Point){
        return when{

        }
    }
}