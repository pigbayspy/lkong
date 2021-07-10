package io.pig.lkong.util

/**
 * @author yinhang
 * @since 2021/7/10
 */
object NumberFormatUtil {
    fun numberToTenKiloString(
        number: Long,
        kiloSuffix: String,
        keepSpace: Boolean,
        keepIfLess: Boolean
    ): String {
        return numberToString(
            number,
            10000,
            kiloSuffix,
            keepSpace,
            keepIfLess
        )
    }

    private fun numberToString(
        number: Long,
        unit: Long,
        suffix: String,
        keepSpace: Boolean,
        keepIfLess: Boolean
    ): String {
        return if (keepIfLess && number < unit) number.toString() else String.format(
            "%.1f%s%s",
            number.toDouble() / unit.toDouble(),
            if (keepSpace) " " else "",
            suffix
        )
    }
}