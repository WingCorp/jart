import basics.Colour

interface AmbientLight {
    val baseColour: Colour
    val intensity: Float
    val colour: Colour
        get() = baseColour * intensity

}