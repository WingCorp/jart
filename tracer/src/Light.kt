interface Light {
    val colour: Colour
    fun direction(p: Point): Vector
    fun shadow( p: Point) : Boolean
    val geometricFactor: Float
    val probabilityDensity: Float
}