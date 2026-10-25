package rentmycar.server.common

import kotlin.math.pow
import kotlin.math.round

// Amounts in euro are always shown with 2 decimals, for example 12.50
const val EURO_DECIMALS = 2

// Rounds a number, for example 0.1452 -> 0.15 (extension: extends Double with a function, "this" is the number)
fun Double.rounded(decimals: Int): Double {
    // Example: 0.1452 with 2 decimals -> factor 100, 14.52, round 15.0, 15.0 / 100 = 0.15
    val factor = 10.0.pow(decimals)
    val bigNumber = this * factor
    val roundedBigNumber = round(bigNumber)

    val result = roundedBigNumber / factor
    return result
}
