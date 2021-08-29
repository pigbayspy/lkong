package io.pig.ui.html

import android.text.Html
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import androidx.core.text.HtmlCompat

object HtmlUtil {

    fun htmlToSpanned(html: String?, imageGetter: ImageGetter, tagHandler: TagHandler): Spanned {
        return if (TextUtils.isEmpty(html)) {
            SpannableString("")
        } else {
            Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler)
        }
    }
}