package io.pig.lkong.util

import androidx.fragment.app.Fragment


/**
 * @author yinhang
 * @since 2021/7/6
 */
object TextSizeUtil {

    fun pxToSp(context: Fragment, px: Int): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (px / scaledDensity + 0.5).toInt()
    }
}