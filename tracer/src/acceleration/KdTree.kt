package acceleration

import Shape
import Tree
import basics.BoundingBox
import basics.Hit
import basics.Ray
import java.util.*

/**
 * A acceleration.KdTree implementation.
 */
class KdTree(shapes: List<Shape>, private val bounds: BoundingBox, useSingleSort: Boolean = false) : Tree {

    class KdNode(
            val shape: Shape,
            val split: Axis,
            val left: KdNode?,
            val right: KdNode?
    )

    private val rootNode: KdNode?

    private val isEmpty: Boolean

    init {
        rootNode = if(useSingleSort) buildSingleSort(shapes) else buildMultipleSorts(shapes, Axis.X)
        isEmpty = rootNode == null
    }

    fun size(): Int {

        fun sizeRec(node: KdNode?, count: Int): Int {

            if (node == null) return@sizeRec count

            return sizeRec(node.right, sizeRec(node.left, count + 1))
        }

        return sizeRec(rootNode, 0)
    }

    override fun intersect(ray: Ray): Hit? {

        if (isEmpty) return null
        var nodesVisited = 0

        fun findNearest(node: KdNode, ray: Ray, bounds: BoundingBox): Hit? {

            if (node.left == null && node.right == null) return node.shape.intersect(ray)

            nodesVisited += 1
            val axis = node.split
            val pivotShape = node.shape
            val (leftBounds, rightBounds) = divideBounds(pivotShape, bounds, axis)
            val targetInLeft = leftBounds.intersect(ray)

            val nearestNode: KdNode?
            val nearestBounds: BoundingBox

            if (targetInLeft) {
                nearestNode = node.left
                nearestBounds = leftBounds
            } else {
                nearestNode = node.right
                nearestBounds = rightBounds
            }

            if (nearestNode == null) return node.shape.intersect(ray)
            return findNearest(nearestNode, ray, nearestBounds)
        }

        return findNearest(rootNode!!, ray, bounds)
    }

    companion object {

        private fun divideBounds(shape: Shape, boundingBox: BoundingBox, axis: Axis): Pair<BoundingBox, BoundingBox> {
            val maxVal = shape.boundingBox.highPoint[axis.dim]
            val minVal = shape.boundingBox.lowPoint[axis.dim]
            val leftPivot = boundingBox.highPoint.immutableSet(axis.dim, maxVal)
            val rightPivot = boundingBox.lowPoint.immutableSet(axis.dim, minVal)
            val leftBounds = BoundingBox(boundingBox.lowPoint, leftPivot)
            val rightBounds = BoundingBox(rightPivot, boundingBox.highPoint)
            return Pair(leftBounds, rightBounds)
        }

        private fun sortByDimension(shapes: List<Shape>, dim: Int) =
                shapes.sortedWith(Comparator { s1, s2 ->
                    s1.boundingBox.centerPoint[dim]
                            .compareTo(s2.boundingBox.centerPoint[dim])
                })

        private fun buildSingleSort(shapes: List<Shape>): KdNode? {

            val sortedByX = sortByDimension(shapes, Axis.X.dim)

            val sortedByY = sortByDimension(shapes, Axis.Y.dim)

            val sortedByZ = sortByDimension(shapes, Axis.Z.dim)

            val shapesByDimension = arrayOf(sortedByX, sortedByY, sortedByZ)

            tailrec fun buildRec(shapes: List<Shape>, axis: Axis): KdNode? {

                if (shapes.isEmpty()) {
                    return null
                }
                if (shapes.size == 1) {
                    return KdNode(shapes.single(), axis, null, null)
                }

                val sortedShapes = shapesByDimension[axis.dim].intersect(shapes).toList()

                val pHalf = sortedShapes.size / 2

                val nextAxis = axis.up()
                return when {
                    sortedShapes.size == 2 -> {
                        KdNode(
                                sortedShapes.last(),
                                axis,
                                buildRec(listOf(sortedShapes.first()), nextAxis),
                                null
                        )
                    }
                    else -> {
                        KdNode(
                                sortedShapes[pHalf],
                                axis,
                                buildRec(sortedShapes.subList(0, pHalf), nextAxis),
                                buildRec(sortedShapes.subList(pHalf + 1, sortedShapes.size), nextAxis)
                        )
                    }
                }
            }

            return buildRec(shapes, Axis.X)
        }

        private fun buildMultipleSorts(shapes: List<Shape>, axis: Axis): KdNode? {
            if (shapes.isEmpty()) {
                return null
            }
            if (shapes.size == 1) {
                return KdNode(shapes.single(), axis, null, null)
            }
            val sortedShapes = shapes.sortedWith(Comparator { s1, s2 ->
                s1.boundingBox.centerPoint[axis.dim]
                        .compareTo(s2.boundingBox.centerPoint[axis.dim])
            })
            val pHalf = sortedShapes.size / 2

            val nextAxis = axis.up()
            return when {
                sortedShapes.size == 2 -> {
                    KdNode(
                            sortedShapes.last(),
                            axis,
                            buildMultipleSorts(listOf(sortedShapes.first()), nextAxis),
                            null
                    )
                }
                else -> {
                    KdNode(
                            sortedShapes[pHalf],
                            axis,
                            buildMultipleSorts(sortedShapes.subList(0, pHalf), nextAxis),
                            buildMultipleSorts(sortedShapes.subList(pHalf + 1, sortedShapes.size), nextAxis)
                    )
                }
            }
        }
    }
}