package io.pig.common.ui.util

import kotlin.math.abs

/**
 * @author yinhang
 * @since 2021/8/15
 */
object FloatUtil {
    private const val EPSILON = 0.00000001f

    fun compareFloats(f1: Float, f2: Float): Boolean {
        return abs(f1 - f2) <= EPSILON
    }
}