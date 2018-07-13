package util

/**
 * A simple Stopwatch implementation
 */
class Stopwatch {

    private val start: Long = System.currentTimeMillis()

    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    fun elapsedTime(): Double {
        val now = System.currentTimeMillis()
        return (now - start) / 1000.0
    }
}