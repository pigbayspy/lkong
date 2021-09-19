package io.pig.lkong.util

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.math.roundToInt

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

    @ColorInt
    fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}