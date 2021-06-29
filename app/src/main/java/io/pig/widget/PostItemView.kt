package io.pig.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import io.pig.lkong.R
import io.pig.lkong.model.PostDisplayModel
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil

/**
 * @author yinhang
 * @since 2021/6/29
 */
class PostItemView : View {

    companion object {
        private const val MOVEMENT_LIMIT_DP = 12
        var px_margin_16 = 0
        var px_margin_72: Int = 0
        var px_width_40: Int = 0
        var px_height_68: Int = 0
        var px_margin_8: Int = 0

        private fun initValid(context: Context) {
            if (px_width_40 == 0) {
                px_margin_16 = UiUtil.dp2px(context, 16f)
                px_margin_72 = UiUtil.dp2px(context, 72f)
                px_width_40 = UiUtil.dp2px(context, 40f)
                px_height_68 = UiUtil.dp2px(context, 68f)
                px_margin_8 = UiUtil.dp2px(context, 8f)
            }
        }
    }

    private val ordinalBound = Rect()
    private val clipBound = Rect()

    private val ordinalPaint: TextPaint
    private val ordinalFontMetrics: Paint.FontMetrics
    private val movementLimitPx: Float

    var postId: Long? = null
    var identityTag: String? = null
    var ordinalText: String? = null
    private var postDisplayCache: PostDisplayModel? = null

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

        initValid(context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.getClipBounds(clipBound)
        if (isInEditMode) {
            return
        }
        val ordinalStr = ordinalText
        if (!ordinalStr.isNullOrEmpty()) {
            ordinalPaint.getTextBounds(ordinalStr.toString(), 0, ordinalStr.length, ordinalBound)
            canvas.drawText(
                ordinalStr, 0, ordinalStr.length,
                (width - px_margin_16 - ordinalBound.width()).toFloat(),
                px_margin_16 - ordinalFontMetrics.top,
                ordinalPaint
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var height = getDesiredHeight()
        val textLayout = postDisplayCache?.textLayout
        if (!isInEditMode && textLayout != null) {
            height += textLayout.height
        }
        setMeasuredDimension(widthSize, height)
    }

    fun setPostDisplayCache(postDisplayModel: PostDisplayModel) {
        this.postDisplayCache = postDisplayModel
        requestLayout()
        post {
            val viewImageMaxWidth = measuredWidth - px_margin_16 * 2
            for (i in postDisplayModel.urlSpanCount..postDisplayModel.importantSpans.size) {
                // Todo
            }
        }
    }

    private fun getDesiredHeight(): Int {
        return px_margin_72 + px_height_68
    }
}