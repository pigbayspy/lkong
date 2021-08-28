package io.pig.ui.html

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import io.pig.lkong.util.ImageLoaderUtil
import java.io.InputStream
import java.util.*

class HtmlTextView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    )

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    constructor(context: Context) : super(context)

    /**
     * Loads HTML from a raw resource, i.e., a HTML file in res/raw/.
     * This allows translatable resource (e.g., res/raw-de/ for german).
     * The containing HTML is parsed to Android's Spannable format and then displayed.
     *
     * @param context context
     * @param id      for example: R.raw.help
     */
    fun setHtmlFromRawResource(context: Context, id: Int, useLocalDrawables: Boolean) {
        // load html from html file from /res/raw
        val inputStreamText = context.resources.openRawResource(id)
        setHtmlFromString(convertStreamToString(inputStreamText), useLocalDrawables)
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays it in this TextView.
     *
     * @param html String containing HTML, for example: "**Hello world!**"
     */
    fun setHtmlFromString(html: String?, useLocalDrawables: Boolean) {
        val imgGetter = if (useLocalDrawables) {
            LocalImageGetter(context)
        } else {
            UrlImageGetter(context, ImageLoaderUtil.IMAGE_LOAD_ALWAYS)
        }
        // this uses Android's Html class for basic parsing, and HtmlTagHandler
        text = Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, imgGetter, HtmlTagHandler())

        // make links work
        movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        const val TAG = "HtmlTextView"
        const val DEBUG = false

        /**
         * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
         *
         * @param inputStream input stream
         * @return string
         */
        private fun convertStreamToString(inputStream: InputStream): String {
            val s = Scanner(inputStream).useDelimiter("\\A")
            return if (s.hasNext()) {
                s.next()
            } else {
                ""
            }
        }
    }
}