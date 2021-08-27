package io.pig.ui.html

import android.graphics.Color
import android.text.Html.ImageGetter
import android.graphics.drawable.Drawable
import android.graphics.drawable.ColorDrawable

class EmptyImageGetter : ImageGetter {
    companion object {
        private val emptyDrawable = ColorDrawable(Color.TRANSPARENT)

        init {
            emptyDrawable.setBounds(0, 0, 0, 0)
        }
    }

    override fun getDrawable(source: String): Drawable {
        return emptyDrawable
    }
}