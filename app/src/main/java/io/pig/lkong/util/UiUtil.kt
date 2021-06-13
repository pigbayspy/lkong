package io.pig.lkong.util

import android.content.Context
import android.util.TypedValue
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/5/16
 */
object UiUtil {

    fun dp2px(context: Context, dp: Float): Int {
        val metrics = context.resources.displayMetrics
        val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp, metrics
        )
        return result.toInt()
    }

    fun getDefaultAvatarSize(context: Context): Int {
        return this.dp2px(
            context,
            context.resources.getDimension(R.dimen.size_avatar_default)
        )
    }
}