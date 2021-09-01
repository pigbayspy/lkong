package io.pig.lkong.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes
import io.pig.lkong.R
import kotlin.math.ceil

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

    /**
     * This padding amount is equal to maxCardElevation + (1 - cos45) * cornerRadius on the sides
     * and maxCardElevation * 1.5 + (1 - cos45) * cornerRadius on top and bottom.
     * 1 - cos(45) = 0.29289321881345247
     *
     * @param cardElevation 卡片数据
     * @param cornerRadius 角度数据
     * @return 值
     */
    fun getCardViewPadding(
        cardElevation: Int,
        cornerRadius: Int
    ): InsetsValue {
        val horizontalPadding =
            ceil(cardElevation.toDouble() + 0.29289321881345247 * cornerRadius.toDouble())
                .toInt()
        val verticalPadding =
            ceil(cardElevation.toDouble() * 1.5 + 0.29289321881345247 * cornerRadius.toDouble())
                .toInt()
        return InsetsValue(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
    }

    fun getSpDimensionPixelSize(context: Context, @DimenRes resId: Int): Float {
        return context.resources.getDimension(resId)
    }

    class InsetsValue(val left: Int, val top: Int, val right: Int, val bottom: Int)
}