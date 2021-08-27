package io.pig.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import io.pig.lkong.R
import io.pig.lkong.model.PostDisplayModel
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.html.AsyncDrawableType
import io.pig.widget.html.EmptySpan
import io.pig.widget.html.ImageSpanContainer
import io.pig.widget.html.PendingImageSpan

/**
 * @author yinhang
 * @since 2021/6/29
 */
class PostItemView : View, ImageSpanContainer {

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

    var postId: String? = null
    var identityTag: String? = null
    var ordinalText: String? = null
    val showImages = false
    private var postDisplayCache: PostDisplayModel? = null
    private var invalidateRunnable: Runnable? = null

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
        postDisplayCache?.authorLayout?.let {
            canvas.save()
            canvas.translate(px_margin_72.toFloat(), px_margin_16.toFloat())
            it.draw(canvas)
            canvas.restore()
        }
        postDisplayCache?.textLayout?.let {
            canvas.save()
            canvas.translate(px_margin_16.toFloat(), px_margin_72.toFloat())
            it.draw(canvas)
            canvas.save()
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
                val pendingImageSpan = postDisplayModel.importantSpans[i] as PendingImageSpan
                pendingImageSpan.loadImage(this, viewImageMaxWidth, Color.argb(255, 229, 229, 229))
            }
        }
    }

    override fun notifyImageSpanLoaded(tag: Any, drawable: Drawable, type: AsyncDrawableType) {
        if (tag != identityTag) {
            return
        }
        // TODO: re-layout and invalidate
        if (invalidateRunnable == null) {
            invalidateRunnable = Runnable {
                if (tag == identityTag) {
                    if (type == AsyncDrawableType.NORMAL) {
                        val charSequence =
                            postDisplayCache!!.textLayout!!.text as SpannableStringBuilder
                        notifyDynamicLayoutChange(charSequence)
                        requestLayout()
                    }
                    invalidate()
                }
            }
            postDelayed(invalidateRunnable, 1000)
        }
    }

    private fun getDesiredHeight(): Int {
        return px_margin_72 + px_height_68
    }

    private fun notifyDynamicLayoutChange(stringBuilder: SpannableStringBuilder) {
        stringBuilder.setSpan(
            EmptySpan(),
            0,
            stringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val emptySpans: Array<EmptySpan> =
            stringBuilder.getSpans(
                0, stringBuilder.length,
                EmptySpan::class.java
            )
        for (emptySpan in emptySpans) {
            stringBuilder.removeSpan(emptySpan)
        }
    }
}