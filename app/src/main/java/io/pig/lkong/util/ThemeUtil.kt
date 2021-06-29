package io.pig.lkong.util

import android.content.Context
import android.graphics.Color

/**
 * @author yinhang
 * @since 2021/6/13
 */
object ThemeUtil {

    fun accentColor(context: Context): Int {
        return Color.parseColor("#263238")
    }

    fun textColorSecondary(context: Context): Int {
        // Todo
        return android.R.attr.textColorSecondary
    }
}