package util

import basics.Colour
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * Created by Jon on 21-07-2017.
 */
object Misc {

    fun<T> id(it: T): T = it

    //Standard background color
    val BG_COLOR: Colour = Colour.BLACK

    //Material util.Misc
    val DEF_MAT_COLOR: Colour = Colour.WHITE

    val DEF_MAT_REFL = 0.0f

    //GAMMA SETTINGS
    val GAMMA_Sdec = 1.0f
    val GAMMA_Senc = 0.45f
    val GAMMA_MY = 1.8f

    //FLOATING POINT ARITHMETIC
    val SMALL_NUM = 0.00000001f
    val SMALL_NUM2 = 0.0001f
    val SMALL_NUM3 = 0.01f
    val SMALL_ONE = 1.0f + 1E-5f
    val SMALL_ZERO = 1E-5f

    //RENDER NUMBER OF REFLECTIONS
    val DEFAULT_REFL_COUNT = 4

    //LIGHT DIFFUSION COEFFICIENT
    val DIFF_COEF = 2.0f

    fun <T> Sequence<T>.papply(
            numberOfThreads: Int = Runtime.getRuntime().availableProcessors(),
            executorService: ExecutorService = Executors.newFixedThreadPool(numberOfThreads),
            procedure: (T) -> Unit) {
        for (element in this) {
            executorService.submit { procedure.invoke(element) }
        }
        executorService.shutdown()
        executorService.awaitTermination(1, TimeUnit.DAYS)
    }

    fun floatEquals(a: Float, b: Float, tolerance: Float = SMALL_NUM2): Boolean {
        return Math.abs(a - b) < tolerance
    }

    fun <T1, T2, R> Collection<T1>.fold2(otherCollection: Collection<T2>, initial: R, folder: (R, T1, T2) -> R): R {
        val smallestSize = min(this.size, otherCollection.size)
        var currentState = initial
        for (i in (0..smallestSize)) {
            currentState = folder(currentState, this.elementAt(i), otherCollection.elementAt(i))
        }
        return currentState
    }
}