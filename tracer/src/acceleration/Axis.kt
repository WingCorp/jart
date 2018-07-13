package acceleration

enum class Axis(val dim: Int) {
    X(0),
    Y(1),
    Z(2);

    fun up(): Axis {
        return when (this) {
            X -> Y
            Y -> Z
            Z -> X
        }
    }

    fun down(): Axis {
        return when (this) {
            X -> Z
            Y -> X
            Z -> Y
        }
    }
}