data class AmbientLight(val baseColour: Colour, val intensity: Float) {
    val colour: Colour
        get() = baseColour * intensity
}