package io.pig.ui.html

import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.SpannableString
import android.text.Spanned

object HtmlUtil {

    fun htmlToSpanned(html: String?, imageGetter: ImageGetter, tagHandler: TagHandler): Spanned {
        return if (html.isNullOrEmpty()) {
            SpannableString("")
        } else {
            HtmlToSpannedUtil.fromHtml(html, imageGetter, tagHandler)
        }
    }
}