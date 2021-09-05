package io.pig.ui.snakebar

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnakeBarUtil {

    fun makeSimple(
        view: View,
        text: CharSequence,
        type: SnakeBarType,
        duration: Int
    ): Snackbar {
        val content: SpannableString = if (text is SpannableString) {
            text
        } else {
            SpannableString(text)
        }
        val start = 0
        val end = content.length
        val foregroundColor = typeToFontColor(type)
        content.setSpan(
            ForegroundColorSpan(foregroundColor),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return Snackbar.make(view, content, duration)
    }

    private fun typeToFontColor(type: SnakeBarType): Int {
        return when (type) {
            SnakeBarType.CONFIRM -> Color.GREEN
            SnakeBarType.WARNING -> Color.YELLOW
            SnakeBarType.ERROR -> Color.RED
            SnakeBarType.INFO -> Color.WHITE
        }
    }
}