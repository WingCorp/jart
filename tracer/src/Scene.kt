import Shapes.Shape

data class Scene(val shapes: List<Shape>, val lights: List<Light>, val ambientLights: List<AmbientLight>)