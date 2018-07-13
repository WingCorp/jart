package acceleration

import BoundingBox
import Point
import org.junit.Assert.assertEquals
import org.junit.Test
import shapes.Sphere
import shapes.Triangle

class KdTreeTest {

    @Test
    fun buildFromShapesTest() {

        val pyramidTop = Point(0f, 3f, 0f)

        val pyramidBack = Point(-1f, 0f, -1f)

        val pyramidFrontLeft = Point(1f, 0f, 0f)

        val pyramidFrontRight = Point(0f, 0f, 1f)

        val pyramid = listOf(
                Triangle(pyramidBack, pyramidFrontLeft, pyramidFrontRight),
                Triangle(pyramidBack, pyramidTop, pyramidFrontLeft),
                Triangle(pyramidFrontLeft, pyramidTop, pyramidFrontRight),
                Triangle(pyramidFrontRight, pyramidTop, pyramidBack)
        )

        val spheres = listOf(
                Sphere(Point(2f, 0f, 3f), 1f),
                Sphere(Point(0f, 2f, -4f), 0.4f)
        )

        val shapes = pyramid + spheres

        val kdTree = KdTree(shapes, BoundingBox(Point(-5f, -1f, -5f), Point(5f, 5f, 5f)))

        assertEquals(shapes.size, kdTree.size())
    }

    @Test
    fun intersect() {
        TODO("Implement this test!")
    }
}