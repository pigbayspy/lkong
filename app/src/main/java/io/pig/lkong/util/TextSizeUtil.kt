package io.pig.lkong.util

import android.content.Context
import androidx.fragment.app.Fragment
import io.pig.lkong.R


/**
 * @author yinhang
 * @since 2021/7/6
 */
object TextSizeUtil {

    const val TEXT_SIZE_HEADLINE = "headline"
    const val TEXT_SIZE_TITLE = "title"
    const val TEXT_SIZE_SUBHEADING = "subheading"
    const val TEXT_SIZE_BODY = "body"
    const val TEXT_SIZE_CAPTION = "caption"
    const val TEXT_SIZE_DISPLAY4 = "display4"
    const val TEXT_SIZE_DISPLAY3 = "display3"
    const val TEXT_SIZE_DISPLAY2 = "display2"
    const val TEXT_SIZE_DISPLAY1 = "display1"


    fun pxToSp(context: Fragment, px: Int): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (px / scaledDensity + 0.5).toInt()
    }

    fun textSizeForMode(
        context: Context,
        key: String,
        mode: String
    ): Int {
        var size: Int = ThemeUtil.prefs(context, key).getInt(mode, 0)
        if (size == 0) {
            size = when (mode) {
                TEXT_SIZE_CAPTION -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_caption
                )
               TEXT_SIZE_BODY -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_body
                )
                TEXT_SIZE_SUBHEADING -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_subheading
                )
                TEXT_SIZE_TITLE -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_title
                )
                TEXT_SIZE_HEADLINE -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_headline
                )
                TEXT_SIZE_DISPLAY1 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display1
                )
                TEXT_SIZE_DISPLAY2 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display2
                )
                TEXT_SIZE_DISPLAY3 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display3
                )
                TEXT_SIZE_DISPLAY4 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display4
                )
                else -> throw IllegalArgumentException("Unknown text size mode: $mode")
            }
        }
        return size
    }
}