import lights.SimpleAmbientLight

data class Scene(
        val shapes: List<Shape>,
        val lights: List<Light>,
        val ambientLights: SimpleAmbientLight
)