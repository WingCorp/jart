package acceleration

import basics.BoundingBox
import basics.Point
import org.junit.Assert.assertEquals
import org.junit.Test
import shapes.Triangle
import util.Stopwatch
import java.util.*
import kotlin.test.assertTrue

class KdTreeTest {

    @Test
    fun buildFromShapesTest() {

        fun makePyramid(scale: Float, center: Point): List<Triangle> {
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

        val random = Random()

        fun randomCenter(): Point = Point(random.nextFloat() * 100f, random.nextFloat() * 100f, random.nextFloat() * 100f)

        val pyramids = Array(18000) {
            makePyramid(random.nextFloat(), randomCenter())
        }.toList().flatten()

        val bounds = BoundingBox(Point(-500f, -500f, -500f), Point(500f, 500f, 500f))

        val multiSortWatch = Stopwatch()

        val multiSortTree = KdTree(pyramids, bounds)

        val multiSortTime = multiSortWatch.elapsedTime()

        println("Constructed KdTree using slow algorithm in $multiSortTime seconds!")

        val singleSortWatch = Stopwatch()

        val singleSortTree = KdTree(pyramids, bounds, true)

        val singleSortTime = singleSortWatch.elapsedTime()

        println("Constructed KdTree using fast algorithm in $singleSortTime seconds!")

        assertEquals(pyramids.size, multiSortTree.size())
        assertEquals(pyramids.size, singleSortTree.size())
        assertTrue {
            multiSortTime > singleSortTime
        }
    }

    @Test
    fun intersect() {
        TODO("Implement this test!")
    }
}