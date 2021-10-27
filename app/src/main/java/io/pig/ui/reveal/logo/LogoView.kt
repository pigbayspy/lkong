package io.pig.ui.reveal.logo

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import io.pig.ui.reveal.TextDrawable

class LogoView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private var logoDrawable: Drawable? = null

    fun setLogo(logoText: String) {
        setLogo(TextDrawable(resources, logoText))
    }

    fun setLogo(@DrawableRes drawableRes: Int) {
        setLogo(ResourcesCompat.getDrawable(resources, drawableRes, null))
    }

    fun setLogo(drawable: Drawable?) {
        logoDrawable = drawable
        logoDrawable?.setBounds(
            0,
            0,
            logoDrawable!!.intrinsicWidth,
            logoDrawable!!.intrinsicHeight
        )
        this.invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (!TextUtils.isEmpty(text)) {
            super.onDraw(canvas)
        } else {
            logoDrawable?.draw(canvas)
        }
    }
}