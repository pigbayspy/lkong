package io.pig.widget

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import io.pig.lkong.R
import io.pig.lkong.util.ThemeUtil

/**
 * @author yinhang
 * @since 2021/6/29
 */
class PostItemView : View {

    companion object {
        private const val MOVEMENT_LIMIT_DP = 12
    }

    private val ordinalPaint: TextPaint
    private val ordinalFontMetrics: Paint.FontMetrics
    private val movementLimitPx: Float

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        ordinalPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        ordinalPaint.textSize = resources.getDimension(R.dimen.text_size_caption)
        ordinalPaint.color = ThemeUtil.textColorSecondary(context)
        ordinalFontMetrics = ordinalPaint.fontMetrics
        // initTouchHandler
        val displayMetrics = context.resources.displayMetrics
        val displayDensity = displayMetrics.density
        movementLimitPx = MOVEMENT_LIMIT_DP * displayDensity
        // init
        setLayerType(LAYER_TYPE_NONE, null)
        isDrawingCacheEnabled = false
    }
}