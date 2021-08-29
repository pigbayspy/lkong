package io.pig.widget.html

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.graphics.drawable.Drawable
import android.graphics.Paint.FontMetricsInt
import java.lang.ref.WeakReference

abstract class DynamicDrawableSpanWithoutSpacing : ReplacementSpan {
    private val verticalAlignment1: Int
    private var mDrawableRef: WeakReference<Drawable?>? = null

    constructor() {
        verticalAlignment1 = ALIGN_BOTTOM
    }

    /**
     * @param verticalAlignment one of [.ALIGN_BOTTOM] or [.ALIGN_BASELINE].
     */
    protected constructor(verticalAlignment: Int) {
        verticalAlignment1 = verticalAlignment
    }

    /**
     * Your subclass must implement this method to provide the bitmap
     * to be drawn.  The dimensions of the bitmap must be the same
     * from each call to the next.
     */
    abstract val drawable: Drawable

    override fun getSize(
        paint: Paint, text: CharSequence,
        start: Int, end: Int,
        fm: FontMetricsInt?
    ): Int {
        val d = cachedDrawable
        val rect = d.bounds
        val ratio = 1.0f
        val newAscent = ((rect.bottom - rect.top).toFloat() / ratio).toInt()
        if (fm != null) {
            fm.ascent = -newAscent
            fm.descent = 0
            fm.top = -newAscent //fm.ascent;
            fm.bottom = 0
        }
        return rect.right
    }

    override fun draw(
        canvas: Canvas, text: CharSequence,
        start: Int, end: Int, x: Float,
        top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val b = cachedDrawable
        canvas.save()
        val drawableBottom = b.bounds.bottom
        var transY = bottom - drawableBottom
        if (verticalAlignment1 == ALIGN_BASELINE) {
            transY -= paint.fontMetricsInt.descent
        }
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    private val cachedDrawable: Drawable
        get() {
            val wr = mDrawableRef
            var d: Drawable? = null
            if (wr != null) {
                d = wr.get()
            }
            if (d == null) {
                d = drawable
                mDrawableRef = WeakReference(d)
            }
            return d
        }

    companion object {
        /**
         * A constant indicating that the bottom of this span should be aligned
         * with the bottom of the surrounding text, i.e., at the same level as the
         * lowest descender in the text.
         */
        const val ALIGN_BOTTOM = 0

        /**
         * A constant indicating that the bottom of this span should be aligned
         * with the baseline of the surrounding text.
         */
        const val ALIGN_BASELINE = 1
    }
}