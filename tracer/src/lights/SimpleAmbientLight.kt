package lights

import AmbientLight
import basics.Colour

data class SimpleAmbientLight(override val baseColour: Colour, override val intensity: Float) : AmbientLight