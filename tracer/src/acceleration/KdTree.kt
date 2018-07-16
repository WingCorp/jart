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
class KdTree(shapes: List<Shape>, private val bounds: BoundingBox) : Tree {

    class KdNode(
            val shape: Shape,
            val split: Axis,
            val left: KdNode?,
            val right: KdNode?
    )

    private val rootNode: KdNode?

    private val isEmpty: Boolean

    init {
        rootNode = build(shapes, Axis.X)
        isEmpty = rootNode == null
    }

    fun size(): Int {
        return inOrderFold(0) { acc, node ->
            if (node == null) acc
            else acc + 1
        }
    }

    override fun intersect(ray: Ray): Hit? {

        if (isEmpty) return null

        tailrec fun findNearest(node: KdNode, ray: Ray, bounds: BoundingBox): Hit? {

            if (node.left == null && node.right == null) return node.shape.intersect(ray)

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

            if (nearestNode != null && nearestNode.shape.boundingBox.intersect(ray)) {
                return nearestNode.shape.intersect(ray)
            }

            if (nearestNode == null) {
                return node.shape.intersect(ray)
            }

            return findNearest(nearestNode, ray, nearestBounds)
        }

        return findNearest(rootNode!!, ray, bounds)
    }

    private fun<T> inOrderFold(initial: T, folder: (T, KdNode?) -> T): T {

        fun inOrderFoldRec(acc: T, node:KdNode?): T {
            if (node == null) {
                return acc
            }
            return inOrderFoldRec(folder(inOrderFoldRec(acc, node.left), node), node.right)
        }

        return inOrderFoldRec(initial, rootNode)
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

        private fun build(shapes: List<Shape>, axis: Axis): KdNode? {
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
                            build(listOf(sortedShapes.first()), nextAxis),
                            null
                    )
                }
                else -> {
                    KdNode(
                            sortedShapes[pHalf],
                            axis,
                            build(sortedShapes.subList(0, pHalf), nextAxis),
                            build(sortedShapes.subList(pHalf + 1, sortedShapes.size), nextAxis)
                    )
                }
            }
        }
    }
}