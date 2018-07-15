package acceleration

import Shape
import Tree
import basics.BoundingBox
import basics.Hit
import basics.Point
import basics.Ray
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

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

    fun drawSplits(path: String) {
        if (!path.endsWith(".png")) {
            error("Can only draw splits to .png file.")
        }

        val initialBounds = BoundingBox(
                Point(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                Point(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        )
        val actualBounds = inOrderFold(initialBounds) {acc, node ->
            if (node == null) return@inOrderFold acc

            val accMin = acc.lowPoint
            val accMax = acc.highPoint

            val min = node.shape.boundingBox.lowPoint
            val max = node.shape.boundingBox.highPoint

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

            return@inOrderFold BoundingBox(outMin, outMax)
        }

        fun drawDimension(axis: Axis) {

            val (widthDim, heightDim) = Pair(axis.down().dim, axis.up().dim)

            val imgSize = 1000

            val bImg = BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB)
            val graphics = bImg.createGraphics()

            graphics.color = Color.WHITE

            graphics.fillRect(0, 0, imgSize, imgSize)

            fun normalize(f: Float, dim: Int): Int {
                val min = actualBounds.lowPoint[dim]
                val max = actualBounds.highPoint[dim]
                return (((f - min) / (max - min)) * imgSize).roundToInt()
            }

            fun drawSplitAndNode(kdNode: KdNode?) {
                if (kdNode == null) {
                    return
                }
                val low = kdNode.shape.boundingBox.lowPoint
                val high = kdNode.shape.boundingBox.highPoint
                val center = kdNode.shape.boundingBox.centerPoint

                val x = normalize(low[widthDim], widthDim)
                val y = normalize(low[heightDim], heightDim)

                val width = normalize(high[widthDim], widthDim)
                val height = normalize(high[heightDim], heightDim)

                val splitX = normalize(center[widthDim], widthDim)
                val splitY = normalize(center[heightDim], heightDim)

//                graphics.color = Color.RED
//                graphics.drawLine(xy1.first, xy1.second, xy2.first, xy2.second)
                graphics.color = Color.WHITE
                graphics.fillRect(x, y, width, height)
                graphics.color = Color.RED
                graphics.drawRect(x, y, width, height)
            }

            inOrderFold(graphics) {g, node ->
                drawSplitAndNode(node)
                g
            }
            val filePath = path.replace(".png", "${axis.name}.png")

            ImageIO.write(bImg, "png", File(filePath))
        }

        Axis.values().forEach { drawDimension(it) }
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