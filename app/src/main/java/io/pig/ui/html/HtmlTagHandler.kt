package io.pig.ui.html

import android.text.Editable
import android.text.Html.TagHandler
import android.text.Layout
import android.text.Spannable
import android.text.style.AlignmentSpan
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.text.style.TypefaceSpan
import android.util.Log
import org.xml.sax.XMLReader
import java.util.*

/**
 * Some parts of this code are based on android.text.Html
 */
class HtmlTagHandler : TagHandler {
    private var listItemCount = 0
    private val listParents = Vector<String>()

    private class Code
    private class Center

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (opening) {
            // opening tag
            if (HtmlTextView.DEBUG) {
                Log.d(HtmlTextView.TAG, "opening, output: $output")
            }
            if (tag.equals("ul", ignoreCase = true) || tag.equals(
                    "ol",
                    ignoreCase = true
                ) || tag.equals("dd", ignoreCase = true)
            ) {
                listParents.add(tag)
                listItemCount = 0
            } else if (tag.equals("code", ignoreCase = true)) {
                start(output, Code())
            } else if (tag.equals("center", ignoreCase = true)) {
                start(output, Center())
            }
        } else {
            // closing tag
            if (HtmlTextView.DEBUG) {
                Log.d(HtmlTextView.TAG, "closing, output: $output")
            }
            if (tag.equals("ul", ignoreCase = true) || tag.equals(
                    "ol",
                    ignoreCase = true
                ) || tag.equals("dd", ignoreCase = true)
            ) {
                listParents.remove(tag)
                listItemCount = 0
            } else if (tag.equals("li", ignoreCase = true)) {
                handleListTag(output)
            } else if (tag.equals("code", ignoreCase = true)) {
                end(output, Code::class.java, TypefaceSpan("monospace"), false)
            } else if (tag.equals("center", ignoreCase = true)) {
                end(
                    output,
                    Center::class.java,
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    true
                )
            }
        }
    }

    /**
     * Mark the opening tag by using private classes
     *
     * @param output output
     * @param mark mark
     */
    private fun start(output: Editable, mark: Any) {
        val len = output.length
        output.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK)
        if (HtmlTextView.DEBUG) {
            Log.d(HtmlTextView.TAG, "len: $len")
        }
    }

    private fun end(output: Editable, kind: Class<*>, repl: Any, paragraphStyle: Boolean) {
        val obj = getLast(output, kind)
        // start of the tag
        val where = output.getSpanStart(obj)
        // end of the tag
        var len = output.length
        output.removeSpan(obj)
        if (where != len) {
            // paragraph styles like AlignmentSpan need to end with a new line!
            if (paragraphStyle) {
                output.append("\n")
                len++
            }
            output.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (HtmlTextView.DEBUG) {
            Log.d(HtmlTextView.TAG, "where: $where")
            Log.d(HtmlTextView.TAG, "len: $len")
        }
    }

    /**
     * Get last marked position of a specific tag kind (private class)
     *
     * @param text test
     * @param kind kind
     * @return last
     */
    private fun getLast(text: Editable, kind: Class<*>): Any? {
        val objects = text.getSpans(0, text.length, kind)
        if (objects.isEmpty()) {
            return null
        }
        for (i in objects.size downTo 1) {
            if (text.getSpanFlags(objects[i - 1]) == Spannable.SPAN_MARK_MARK) {
                return objects[i - 1]
            }
        }
        return null
    }

    private fun handleListTag(output: Editable) {
        if (listParents.lastElement() == "ul") {
            output.append("\n")
            val split = output.toString().split("\n".toRegex()).toTypedArray()
            val lastIndex = split.size - 1
            val start = output.length - split[lastIndex].length - 1
            output.setSpan(BulletSpan(15 * listParents.size), start, output.length, 0)
        } else if (listParents.lastElement() == "ol") {
            listItemCount++
            output.append("\n")
            val split = output.toString().split("\n".toRegex()).toTypedArray()
            val lastIndex = split.size - 1
            val start = output.length - split[lastIndex].length - 1
            output.insert(start, "$listItemCount. ")
            output.setSpan(
                LeadingMarginSpan.Standard(15 * listParents.size),
                start,
                output.length,
                0
            )
        }
    }
}