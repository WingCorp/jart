package acceleration

import basics.BoundingBox
import basics.Point
import basics.Ray
import basics.Vector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import shapes.Triangle
import util.Stopwatch
import java.io.File
import java.util.*

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

    private fun randomCenter(): Point =
            Point(random.nextFloat() * 100f, random.nextFloat() * 100f, random.nextFloat() * 100f)

    @Test
    fun buildFromShapesTest() {

        val pyramids = Array(18000) {
            makePyramid(random.nextFloat(), randomCenter())
        }.toList().flatten()

        val bounds = BoundingBox(Point(-500f, -500f, -500f), Point(500f, 500f, 500f))

        val tree = KdTree(pyramids, bounds)

        assertEquals(pyramids.size, tree.size())
    }

    @Test
    fun drawSplitsTest() {
        val pyramids = Array(18000) {
            makePyramid(random.nextFloat(), randomCenter())
        }.toList().flatten()

        val bounds = BoundingBox(Point(-500f, -500f, -500f), Point(500f, 500f, 500f))

        val tree = KdTree(pyramids, bounds)

        tree.drawSplits("./testSplits.png")

        val xFile = File("./testSplitsX.png")

        val yFile = File("./testSplitsY.png")

        val zFile = File("./testSplitsZ.png")

        assertTrue(xFile.exists() && yFile.exists() && zFile.exists())
    }

    @Test
    fun intersect() {
        val target = Triangle(Point(0f, 0f, 0f), Point(1f, 0f, 0f), Point(0f, 1f, 0f))
        val ray = Ray(Point(0.3f, 0.3f, 400f), Vector(0f, 0f, -1f))

        val intersects = target.intersect(ray) != null

        assertTrue(intersects)

        val pyramids = Array(18000) {
            makePyramid(random.nextFloat(), randomCenter())
        }.toList().flatten()

        val bounds = BoundingBox(Point(-500f, -500f, -500f), Point(500f, 500f, 500f))

        val stopwatch = Stopwatch()

        val tree = KdTree(pyramids, bounds)

        val buildTime = stopwatch.elapsedTime()
        println("Constructing KdTree for ${pyramids.size} triangles took $buildTime seconds.")

        val kdHit = tree.intersect(ray)

        assertTrue(kdHit != null)
    }
}