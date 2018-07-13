import basics.Hit
import basics.Ray

interface Tree {
    fun intersect(ray: Ray): Hit?
}