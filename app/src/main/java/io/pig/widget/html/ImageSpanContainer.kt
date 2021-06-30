package io.pig.widget.html

import android.graphics.drawable.Drawable

/**
 * @author yinhang
 * @since 2021/6/30
 */
interface ImageSpanContainer {
    fun notifyImageSpanLoaded(tag: Any, drawable: Drawable, type: AsyncDrawableType)
}