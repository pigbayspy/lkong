package io.pig.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/10
 */
class BorderCircleView(
    context: Context,
    attributeSet: AttributeSet,
    defStyleAttr: Int
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private val check: Drawable
    private val paint: Paint
    private val paintBorder: Paint
    private val borderWidth: Int

    private var paintCheck: Paint? = null
    private var blackFilter: PorterDuffColorFilter? = null
    private var whiteFilter: PorterDuffColorFilter? = null

    init {
        check = ContextCompat.getDrawable(context, R.drawable.ic_setting_theme_check)!!
        borderWidth = resources.getDimension(R.dimen.setting_theme_circle_view_border).toInt()
        paint = Paint()
        paint.isAntiAlias = true
        paintBorder = Paint()
        paintBorder.isAntiAlias = true
        paintBorder.color = Color.BLACK
        setWillNotDraw(false)
    }

    override fun setBackgroundColor(color: Int) {
        paint.color = color
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = width
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec))
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var canvasSize = width
        if (height < canvasSize) {
            canvasSize = height
        }

        val circleCenter = (canvasSize - borderWidth * 2) / 2
        canvas?.apply {
            drawCircle(
                (circleCenter + borderWidth).toFloat(),
                (circleCenter + borderWidth).toFloat(),
                (canvasSize - borderWidth * 2) / 2 + borderWidth - 4.0f,
                paintBorder
            )
            drawCircle(
                (circleCenter + borderWidth).toFloat(),
                (circleCenter + borderWidth).toFloat(),
                (canvasSize - borderWidth * 2) / 2 - 4.0f,
                paint
            )
        }
        if (isActivated) {
            val offset: Int = canvasSize / 2 - check.intrinsicWidth / 2

            if (paintCheck == null) {
                val newPaintCheck = Paint()
                newPaintCheck.isAntiAlias = true
                paintCheck = Paint()
            }
            if (whiteFilter == null || blackFilter == null) {
                blackFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                whiteFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }
            paintCheck?.apply {
                val filter = if (paint.color == Color.WHITE) {
                    blackFilter
                } else {
                    whiteFilter
                }
                colorFilter = filter
            }
            check.setBounds(
                offset,
                offset,
                check.intrinsicWidth - offset,
                check.intrinsicHeight - offset
            )
            check.draw(canvas!!)
        }
    }
}