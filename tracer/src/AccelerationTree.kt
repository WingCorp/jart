/**
 * A `AccelarationTree` implementation.
 */
class AccelarationTree(shapes: List<Shape>, private val bounds: BoundingBox) {

    enum class Axis {
        X,
        Y,
        Z
    }

    class KdNode(
            val shape: Shape,
            val split: Axis,
            val left: KdNode?,
            val right: KdNode?
    )

    private val rootNode: KdNode?

    val isEmpty: Boolean

    init {
        rootNode = build(shapes, Axis.X)
        isEmpty = rootNode == null
    }

    fun intersect(ray: Ray): Hit? {

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
            }
            else {
                nearestNode = node.right
                nearestBounds = rightBounds
            }

            if (nearestNode == null) return node.shape.intersect(ray)
            return findNearest(nearestNode, ray, nearestBounds)
        }

        return findNearest(rootNode!!, ray, bounds)
    }

    companion object {

        private fun nextAxis(axis: Axis): Axis =
                when (axis) {
                    Axis.X -> Axis.Y
                    Axis.Y -> Axis.Z
                    Axis.Z -> Axis.X
                }

        private fun comparePointByAxis(axis: Axis) = { p: Point, q: Point ->
            when (axis) {
                Axis.X -> p.x.compareTo(q.x)
                Axis.Y -> p.y.compareTo(q.y)
                Axis.Z -> p.z.compareTo(q.z)
            }
        }

        private fun getPointValByAxis(point: Point, axis: Axis): Float {
            return when (axis) {
                Axis.X -> point.x
                Axis.Y -> point.y
                Axis.Z -> point.z
            }
        }

        private fun makePivotByAxis(point: Point, pivot: Float, axis: Axis): Point {
            return when (axis) {
                Axis.X -> Point(pivot, point.y, point.z)
                Axis.Y -> Point(point.x, pivot, point.z)
                Axis.Z -> Point(point.x, point.y, pivot)
            }
        }

        private fun divideBounds(shape: Shape, boundingBox: BoundingBox, axis: Axis): Pair<BoundingBox, BoundingBox> {
            val maxVal = getPointValByAxis(shape.boundingBox.highPoint, axis)
            val minVal = getPointValByAxis(shape.boundingBox.lowPoint, axis)
            val leftPivot = makePivotByAxis(boundingBox.highPoint, maxVal, axis)
            val rightPivot = makePivotByAxis(boundingBox.lowPoint, minVal, axis)
            val leftBounds = BoundingBox(boundingBox.lowPoint, leftPivot)
            val rightBounds = BoundingBox(rightPivot, boundingBox.highPoint)
            return Pair(leftBounds, rightBounds)
        }

        fun build(shapes: List<Shape>, axis: Axis): KdNode? {
            if (shapes.isEmpty()) {
                return null
            }
            if (shapes.size == 1) {
                return KdNode(shapes.single(), axis, null, null)
            }
            val sortedShapes = shapes.sortedWith(Comparator { s1, s2 ->
                comparePointByAxis(axis)
                        .invoke(s1.boundingBox.centerPoint, s2.boundingBox.centerPoint)
            })
            val pHalf = sortedShapes.size / 2
            val nextAxis = nextAxis(axis)
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