package io.pig.lkong.util

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * @author yinhang
 * @since 2021/7/11
 */
object ColorUtil {

    @ColorInt
    fun darkenColor(@ColorInt color: Int): Int {
        return shiftColor(color, 0.9f)
    }

    @ColorInt
    private fun shiftColor(@ColorInt color: Int, scale: Float): Int {
        if (scale == 1f) {
            return color
        }
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= scale // value component
        return Color.HSVToColor(hsv)
    }
}