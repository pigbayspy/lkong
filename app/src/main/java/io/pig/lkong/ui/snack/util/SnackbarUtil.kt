package io.pig.lkong.ui.snack.util

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar
import io.pig.lkong.ui.snack.em.SnackTypeEnum

/**
 * @author yinhang
 * @since 2021/05/18
 */
object SnackBarUtil {

    @ColorInt
    private fun typeToFontColor(type: SnackTypeEnum): Int {
        return when (type) {
            SnackTypeEnum.CONFIRM -> Color.GREEN
            SnackTypeEnum.WARNING -> Color.YELLOW
            SnackTypeEnum.ERROR -> Color.RED
            SnackTypeEnum.INFO -> Color.WHITE
        }
    }

    fun makeSimple(view: View, text: CharSequence, type: SnackTypeEnum, duration: Int): Snackbar {
        val content: SpannableString = if (text is SpannableString) {
            text
        } else {
            SpannableString(text)
        }
        val start = 0
        val end = content.length
        val foregroundColor: Int = typeToFontColor(type)
        content.setSpan(
            ForegroundColorSpan(foregroundColor),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return Snackbar.make(view, content, duration)
    }
}