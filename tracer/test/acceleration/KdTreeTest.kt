package acceleration

import Shape
import basics.BoundingBox
import basics.Point
import basics.Ray
import basics.Vector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import shapes.Triangle
import util.Stopwatch
import java.util.*
import kotlin.test.assertNotNull

class KdTreeTest {

    private fun makePyramid(scale: Float, center: Point): List<Triangle> {
        val pyramidTop = (Point(0f, 3f, 0f) * scale) + center

        val pyramidBack = (Point(-1f, 0f, -1f) * scale) + center

        val pyramidFrontLeft = (Point(1f, 0f, 0f) * scale) + center

        val pyramidFrontRight = (Point(0f, 0f, 1f) * scale) + center

        return listOf(
                Triangle(pyramidBack, pyramidFrontLeft, pyramidFrontRight),
                Triangle(pyramidBack, pyramidTop, pyramidFrontLeft),
                Triangle(pyramidFrontLeft, pyramidTop, pyramidFrontRight),
                Triangle(pyramidFrontRight, pyramidTop, pyramidBack)
        )
    }

    private val random = Random()

    private fun randomPoint(): Point =
            Point(random.nextFloat() * 100f, random.nextFloat() * 100f, random.nextFloat() * 100f)

    private fun randomVector(): Vector =
            Vector(random.nextFloat() * 100f, random.nextFloat() * 100f, random.nextFloat() * 100f)

    private fun determineBounds(shapes: Collection<Shape>): BoundingBox {
        val initialBounds = BoundingBox(
                Point(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                Point(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        )
        return shapes.fold(initialBounds) { acc, shape ->
            val accMin = acc.lowPoint
            val accMax = acc.highPoint

            val min = shape.boundingBox.lowPoint
            val max = shape.boundingBox.highPoint

            var outMin = accMin
            var outMax = accMax

            for (dim in 0..2) {
                if (min[dim] < accMin[dim]) {
                    outMin = outMin.immutableSet(dim, min[dim])
                }
                if (max[dim] > accMax[dim]) {
                    outMax = outMax.immutableSet(dim, max[dim])
                }
            }
            return@fold BoundingBox(outMin, outMax)
        }
    }

    private fun generatePyramids(i: Int): Pair<List<Triangle>, BoundingBox> {
        val pyramids = Array(i) {
            makePyramid(random.nextFloat(), randomPoint())
        }.toList().flatten()
        return Pair(
                pyramids,
                determineBounds(pyramids)
        )
    }

    @Test
    fun buildFromShapesTest() {

        val (pyramids, bounds) = generatePyramids(20000)

        val tree = KdTree(pyramids, bounds)

        assertEquals(pyramids.size, tree.size())
    }

    @Test
    fun intersect() {
        val target = Triangle(Point(0f, 0f, 0f), Point(1f, 0f, 0f), Point(0f, 1f, 0f))
        val ray = Ray(Point(0.3f, 0.3f, 400f), Vector(0f, 0f, -1f))

        val intersects = target.intersect(ray) != null

        assertTrue(intersects)

        val (pyramids, bounds) = generatePyramids(40000)

        val stopwatch = Stopwatch()

        val tree = KdTree(pyramids + target, bounds)

        val buildTime = stopwatch.elapsedTime()
        println("Constructing KdTree for ${pyramids.size} triangles took $buildTime seconds.")

        val intersectWatch = Stopwatch()
        val hit = tree.intersect(ray)
        println("Intersecting KdTree with one ray took ${intersectWatch.elapsedTime()} seconds.")

        assertNotNull(hit)
    }

    @Test
    fun testManyIntersects() {
        val rays = Array(1024 * 512) {
            Ray(randomPoint(), randomVector())
        }

        val (pyramids, bounds) = generatePyramids(250000)

        val buildWatch = Stopwatch()
        val tree = KdTree(pyramids, bounds)
        val buildTime = buildWatch.elapsedTime()

        println("Constructed kdtree for ${pyramids.size} triangles in $buildTime")

        val intersectWatch = Stopwatch()
        for (ray in rays) {
            tree.intersect(ray)
        }
        val intersectTime = intersectWatch.elapsedTime()

        println("Intersected ${rays.size} rays in $intersectTime")
    }
}