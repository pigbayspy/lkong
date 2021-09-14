package io.pig.ui.html

import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.Spanned
import org.ccil.cowan.tagsoup.HTMLSchema
import org.ccil.cowan.tagsoup.Parser

object HtmlToSpannedUtil {

    private val schema = HTMLSchema()

    fun fromHtml(source: String, imageGetter: ImageGetter?, tagHandler: TagHandler): Spanned {
        val parser = Parser()
        try {
            parser.setProperty(Parser.schemaProperty, schema)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        val converter = HtmlToSpannedConverter(source, imageGetter, tagHandler, parser)
        return converter.convert()
    }
}