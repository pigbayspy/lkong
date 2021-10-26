package io.pig.ui.reveal

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue

class TextDrawable(resources: Resources, private val mText: String) : Drawable() {

    private val paint: Paint
    private val intrinsicWidth: Int
    private val intrinsicHeight: Int
    private val textSize: Float

    override fun draw(canvas: Canvas) {
        val r = bounds
        val count = canvas.save()
        canvas.translate(r.left.toFloat(), r.top.toFloat())
        val height = if (canvas.height < 0) {
            r.height()
        } else {
            canvas.height
        }
        canvas.drawText(mText, 0f, height / 2 - (paint.descent() + paint.ascent()) / 2, paint)
        canvas.restoreToCount(count)
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return intrinsicHeight
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            24f, resources.displayMetrics
        )
        paint = Paint()
        paint.color = Color.GRAY
        paint.textSize = textSize
        paint.isAntiAlias = true
        paint.isFakeBoldText = false
        paint.setShadowLayer(2f, 0f, 0f, Color.BLACK)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.LEFT
        val bounds = Rect()
        paint.getTextBounds(mText, 0, mText.length, bounds)
        intrinsicWidth = bounds.width()
        intrinsicHeight = bounds.height()
    }
}