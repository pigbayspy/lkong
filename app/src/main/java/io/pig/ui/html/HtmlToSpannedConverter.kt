package io.pig.ui.html

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.text.Html.TagHandler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.ParagraphStyle
import android.text.style.QuoteSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.core.content.res.ResourcesCompat
import io.pig.lkong.R
import org.ccil.cowan.tagsoup.Parser
import org.xml.sax.Attributes
import org.xml.sax.ContentHandler
import org.xml.sax.InputSource
import org.xml.sax.Locator
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import java.util.*

class HtmlToSpannedConverter(
    private val mSource: String,
    private val imageGetter: ImageGetter,
    private val tagHandler: TagHandler,
    private val parser: Parser
) : ContentHandler {

    private val spannableStringBuilder = SpannableStringBuilder()

    fun convert(): Spanned {
        parser.contentHandler = this
        try {
            parser.parse(InputSource(StringReader(mSource)))
        } catch (e: IOException) {
            // We are reading from a string. There should not be IO problems.
            // TagSoup doesn't throw parse exceptions.
            throw RuntimeException(e)
        } catch (e: SAXException) {
            throw RuntimeException(e)
        }
        // Fix flags and range for paragraph-type markup.
        val obj: Array<ParagraphStyle> = spannableStringBuilder.getSpans(
            0,
            spannableStringBuilder.length,
            ParagraphStyle::class.java
        )
        for (o in obj) {
            val start = spannableStringBuilder.getSpanStart(o)
            var end = spannableStringBuilder.getSpanEnd(o)

            // If the last line of the range is blank, back off by one.
            if (end - 2 >= 0) {
                if (spannableStringBuilder[end - 1] == '\n' &&
                    spannableStringBuilder[end - 2] == '\n'
                ) {
                    end--
                }
            }
            if (end == start) {
                spannableStringBuilder.removeSpan(o)
            } else {
                spannableStringBuilder.setSpan(o, start, end, Spannable.SPAN_PARAGRAPH)
            }
        }
        return spannableStringBuilder
    }

    private fun handleStartTag(tag: String, attributes: Attributes) {
        if (tag.equals("br", ignoreCase = true)) {
            // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
            // so we can safely emite the linebreaks when we handle the close tag.
        } else if (tag.equals("p", ignoreCase = true)) {
            handleP(spannableStringBuilder)
        } else if (tag.equals("div", ignoreCase = true)) {
            handleP(spannableStringBuilder)
        } else if (tag.equals("strong", ignoreCase = true)) {
            start(spannableStringBuilder, Bold())
        } else if (tag.equals("b", ignoreCase = true)) {
            start(spannableStringBuilder, Bold())
        } else if (tag.equals("em", ignoreCase = true)) {
            start(spannableStringBuilder, Italic())
        } else if (tag.equals("cite", ignoreCase = true)) {
            start(spannableStringBuilder, Italic())
        } else if (tag.equals("dfn", ignoreCase = true)) {
            start(spannableStringBuilder, Italic())
        } else if (tag.equals("i", ignoreCase = true)) {
            start(spannableStringBuilder, Italic())
        } else if (tag.equals("big", ignoreCase = true)) {
            start(spannableStringBuilder, Big())
        } else if (tag.equals("small", ignoreCase = true)) {
            start(spannableStringBuilder, Small())
        } else if (tag.equals("font", ignoreCase = true)) {
            startFont(spannableStringBuilder, attributes)
        } else if (tag.equals("blockquote", ignoreCase = true)) {
            handleP(spannableStringBuilder)
            start(spannableStringBuilder, Blockquote())
        } else if (tag.equals("tt", ignoreCase = true)) {
            start(spannableStringBuilder, Monospace())
        } else if (tag.equals("a", ignoreCase = true)) {
            startA(spannableStringBuilder, attributes)
        } else if (tag.equals("u", ignoreCase = true)) {
            start(spannableStringBuilder, Underline())
        } else if (tag.equals("sup", ignoreCase = true)) {
            start(spannableStringBuilder, Super())
        } else if (tag.equals("sub", ignoreCase = true)) {
            start(spannableStringBuilder, Sub())
        } else if (tag.length == 2 && Character.toLowerCase(tag[0]) == 'h' && tag[1] >= '1' && tag[1] <= '6') {
            handleP(spannableStringBuilder)
            start(
                spannableStringBuilder, Header(
                    tag[1] - '1'
                )
            )
        } else if (tag.equals("img", ignoreCase = true)) {
            startImg(spannableStringBuilder, attributes, imageGetter)
        } else tagHandler.handleTag(true, tag, spannableStringBuilder, parser)
    }

    private fun handleEndTag(tag: String) {
        if (tag.equals("br", ignoreCase = true)) {
            handleBr(spannableStringBuilder)
        } else if (tag.equals("p", ignoreCase = true)) {
            handleP(spannableStringBuilder)
        } else if (tag.equals("div", ignoreCase = true)) {
            handleP(spannableStringBuilder)
        } else if (tag.equals("strong", ignoreCase = true)) {
            end(spannableStringBuilder, Bold::class.java, StyleSpan(Typeface.BOLD))
        } else if (tag.equals("b", ignoreCase = true)) {
            end(spannableStringBuilder, Bold::class.java, StyleSpan(Typeface.BOLD))
        } else if (tag.equals("em", ignoreCase = true)) {
            end(spannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("cite", ignoreCase = true)) {
            end(spannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("dfn", ignoreCase = true)) {
            end(spannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("i", ignoreCase = true)) {
            end(spannableStringBuilder, Italic::class.java, StyleSpan(Typeface.ITALIC))
        } else if (tag.equals("big", ignoreCase = true)) {
            end(spannableStringBuilder, Big::class.java, RelativeSizeSpan(1.25f))
        } else if (tag.equals("small", ignoreCase = true)) {
            end(spannableStringBuilder, Small::class.java, RelativeSizeSpan(0.8f))
        } else if (tag.equals("font", ignoreCase = true)) {
            endFont(spannableStringBuilder)
        } else if (tag.equals("blockquote", ignoreCase = true)) {
            handleP(spannableStringBuilder)
            end(spannableStringBuilder, Blockquote::class.java, QuoteSpan())
        } else if (tag.equals("tt", ignoreCase = true)) {
            end(
                spannableStringBuilder, Monospace::class.java,
                TypefaceSpan("monospace")
            )
        } else if (tag.equals("a", ignoreCase = true)) {
            endA(spannableStringBuilder)
        } else if (tag.equals("u", ignoreCase = true)) {
            end(spannableStringBuilder, Underline::class.java, UnderlineSpan())
        } else if (tag.equals("sup", ignoreCase = true)) {
            end(spannableStringBuilder, Super::class.java, SuperscriptSpan())
        } else if (tag.equals("sub", ignoreCase = true)) {
            end(spannableStringBuilder, Sub::class.java, SubscriptSpan())
        } else if (tag.length == 2 && Character.toLowerCase(tag[0]) == 'h' && tag[1] >= '1' && tag[1] <= '6') {
            handleP(spannableStringBuilder)
            endHeader(spannableStringBuilder)
        } else {
            tagHandler.handleTag(false, tag, spannableStringBuilder, parser)
        }
    }

    override fun setDocumentLocator(locator: Locator) {}

    override fun startDocument() {
    }

    override fun endDocument() {
    }

    override fun startPrefixMapping(prefix: String, uri: String) {
    }

    override fun endPrefixMapping(prefix: String) {
    }

    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        handleStartTag(localName, attributes)
    }

    override fun endElement(uri: String, localName: String, qName: String) {
        handleEndTag(localName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        val sb = StringBuilder()

        /*
         * Ignore whitespace that immediately follows other whitespace;
         * newlines count as spaces.
         */
        for (i in 0 until length) {
            val c = ch[i + start]
            if (c == ' ' || c == '\n') {
                var pred: Char
                var len = sb.length
                if (len == 0) {
                    len = spannableStringBuilder.length
                    pred = if (len == 0) {
                        '\n'
                    } else {
                        spannableStringBuilder[len - 1]
                    }
                } else {
                    pred = sb[len - 1]
                }
                if (pred != ' ' && pred != '\n') {
                    sb.append(' ')
                }
            } else {
                sb.append(c)
            }
        }
        spannableStringBuilder.append(sb)
    }

    override fun ignorableWhitespace(ch: CharArray, start: Int, length: Int) {
    }

    override fun processingInstruction(target: String, data: String) {
    }

    override fun skippedEntity(name: String) {
    }

    private class Bold
    private class Italic
    private class Underline
    private class Big
    private class Small
    private class Monospace
    private class Blockquote
    private class Super
    private class Sub
    private class Font(val color: String, val face: String)
    private class Href(val href: String)
    private class Header(val level: Int)

    companion object {
        private val HEADER_SIZES = floatArrayOf(1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f)

        private val COLOR_MAP = mapOf(
            "black" to Color.BLACK,
            "darkgray" to Color.DKGRAY,
            "gray" to Color.GRAY,
            "lightgray" to Color.LTGRAY,
            "white" to Color.WHITE,
            "red" to Color.RED,
            "green" to Color.GREEN,
            "blue" to Color.BLUE,
            "yellow" to Color.YELLOW,
            "cyan" to Color.CYAN,
            "magenta" to Color.MAGENTA,
            "aqua" to -0xff0001,
            "fuchsia" to -0xff01,
            "darkgrey" to Color.DKGRAY,
            "grey" to Color.GRAY,
            "lightgrey" to Color.LTGRAY,
            "lime" to -0xff0100,
            "maroon" to -0x800000,
            "navy" to -0xffff80,
            "olive" to -0x7f8000,
            "purple" to -0x7fff80,
            "silver" to -0x3f3f40,
            "teal" to -0xff7f80
        )

        private fun handleP(text: SpannableStringBuilder) {
            val len = text.length
            if (len >= 1 && text[len - 1] == '\n') {
                if (len >= 2 && text[len - 2] == '\n') {
                    return
                }
                text.append("\n")
                return
            }
            if (len != 0) {
                text.append("\n\n")
            }
        }

        private fun handleBr(text: SpannableStringBuilder) {
            text.append("\n")
        }

        private fun <T> getLast(text: Spanned, kind: Class<T>): T? {
            /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
            val objects: Array<T> = text.getSpans(0, text.length, kind)
            return if (objects.isEmpty()) {
                null
            } else {
                objects.last()
            }
        }

        private fun start(text: SpannableStringBuilder, mark: Any) {
            val len = text.length
            text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK)
        }

        private fun <T> end(text: SpannableStringBuilder, kind: Class<T>, repl: Any) {
            val len = text.length
            val obj = getLast(text, kind)
            val where = text.getSpanStart(obj)
            text.removeSpan(obj)
            if (where != len) {
                text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        private fun startImg(
            text: SpannableStringBuilder,
            attributes: Attributes, img: ImageGetter?
        ) {
            val src = attributes.getValue("", "src")
            var d: Drawable? = null
            if (img != null) {
                d = img.getDrawable(src)
            }
            if (d == null) {
                d = ResourcesCompat.getDrawable(
                    Resources.getSystem(),
                    R.drawable.placeholder_loading,
                    null
                )
                d!!.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
            }
            val len = text.length
            text.append("\uFFFC")
            text.setSpan(
                ImageSpan(d, src), len, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        private fun startFont(
            text: SpannableStringBuilder,
            attributes: Attributes
        ) {
            val color = attributes.getValue("", "color")
            val face = attributes.getValue("", "face")
            val len = text.length
            text.setSpan(Font(color, face), len, len, Spannable.SPAN_MARK_MARK)
        }

        private fun endFont(text: SpannableStringBuilder) {
            val len = text.length
            val obj = getLast(text, Font::class.java)
            val where = text.getSpanStart(obj)
            text.removeSpan(obj)
            if (where != len) {
                if (!TextUtils.isEmpty(obj!!.color)) {
                    if (obj.color.startsWith("@")) {
                        val res = Resources.getSystem()
                        val name = obj.color.substring(1)
                        val colorRes = res.getIdentifier(name, "color", "android")
                        if (colorRes != 0) {
                            val colors = res.getColorStateList(colorRes)
                            text.setSpan(
                                TextAppearanceSpan(null, 0, 0, colors, null),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        val c = getHtmlColor(obj.color)
                        if (c != -1) {
                            text.setSpan(
                                ForegroundColorSpan(c or -0x1000000),
                                where, len,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                }

                text.setSpan(TypefaceSpan(obj.face), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        private fun startA(text: SpannableStringBuilder, attributes: Attributes) {
            val href = attributes.getValue("", "href")
            val len = text.length
            text.setSpan(Href(href), len, len, Spannable.SPAN_MARK_MARK)
        }

        private fun endA(text: SpannableStringBuilder) {
            val len = text.length
            val obj = getLast(text, Href::class.java)
            val where = text.getSpanStart(obj)
            text.removeSpan(obj)
            if (where != len) {
                if (obj != null) {
                    text.setSpan(URLSpan(obj.href), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        private fun endHeader(text: SpannableStringBuilder) {
            var len = text.length
            val obj = getLast(text, Header::class.java)
            val where = text.getSpanStart(obj)
            text.removeSpan(obj)

            // Back off not to change only the text, not the blank line.
            while (len > where && text[len - 1] == '\n') {
                len--
            }
            if (where != len && obj != null) {
                text.setSpan(
                    RelativeSizeSpan(HEADER_SIZES[obj.level]),
                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                text.setSpan(
                    StyleSpan(Typeface.BOLD),
                    where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        private fun getHtmlColor(color: String): Int {
            val i = COLOR_MAP[color.lowercase(Locale.ROOT)]
            return i
                ?: try {
                    convertValueToInt(color)
                } catch (nfe: NumberFormatException) {
                    -1
                }
        }

        private fun convertValueToInt(charSeq: CharSequence): Int {
            val nm = charSeq.toString()

            // XXX This code is copied from Integer.decode() so we don't
            // have to instantiate an Integer!
            var sign = 1
            var index = 0
            val len = nm.length
            var base = 10
            if ('-' == nm[0]) {
                sign = -1
                index++
            }
            if ('0' == nm[index]) {
                //  Quick check for a zero by itself
                if (index == len - 1) {
                    return 0
                }
                val c = nm[index + 1]
                if ('x' == c || 'X' == c) {
                    index += 2
                    base = 16
                } else {
                    index++
                    base = 8
                }
            } else if ('#' == nm[index]) {
                index++
                base = 16
            }
            return nm.substring(index).toInt(base) * sign
        }
    }
}