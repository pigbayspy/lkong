package io.pig.ui.html

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * Copied from http://stackoverflow.com/a/22298833
 */
class LocalImageGetter(var context: Context) : ImageGetter {
    override fun getDrawable(source: String): Drawable? {
        var id = context.resources.getIdentifier(source, "drawable", context.packageName)
        if (id == 0) {
            // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
            id = context.resources.getIdentifier(source, "drawable", "android")
        }
        if (id == 0) {
            // prevent a crash if the resource still can't be found
            Log.e(HtmlTextView.TAG, "source could not be found: $source")
            return null
        }
        val d = ContextCompat.getDrawable(context, id) ?: return null
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        return d
    }
}