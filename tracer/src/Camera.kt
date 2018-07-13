import basics.Point
import basics.Vector

data class Camera(
        val position: Point,
        val lookAt: Point,
        val up: Vector,
        val zoom: Float,
        val width: Float,
        val height: Float,
        val resX: Int,
        val resY: Int
)