package io.pig.lkong.ui.edit

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.SpanWatcher
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.text.style.URLSpan
import android.util.Log
import android.widget.EditText
import java.util.*

class ImageEditTextHandler(private val editor: EditText) : TextWatcher {

    private val emoticonsToRemove: MutableList<Any> = ArrayList()
    private var currentChangeStart = 0
    private var currentChangeCount = 0
    private var currentChangeEnd = 0

    fun insert(charSequence: CharSequence) {
        // Get the selected text.
        val start = editor.selectionStart
        val end = editor.selectionEnd
        val message = editor.editableText
        val list = (charSequence as Spanned).getSpans(
            start, end,
            Any::class.java
        )
        for (span in list) {
            Log.d("span", span.javaClass.name)
        }
        // Insert the emoticon.
        message.replace(start, end, charSequence)
    }

    fun insertImageSpan(emoticon: String, drawable: Drawable) {
        // Create the ImageSpan
        val imageSpan = ImageSpan(drawable, emoticon, ImageSpan.ALIGN_BASELINE)
        insertSpan(emoticon, imageSpan)
    }

    fun insertUrlSpan(name: String, value: String?) {
        // Create the ImageSpan
        val urlSpan = URLSpan(value)
        insertSpan(name, urlSpan)
    }

    private fun insertSpan(content: String, span: Any?) {
        // Get the selected text.
        val start = editor.selectionStart
        val end = editor.selectionEnd
        val message = editor.editableText

        // Insert the emoticon.
        message.replace(start, end, content)
        message.setSpan(span, start, start + content.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        // Check if some text will be removed.
        currentChangeStart = start
        currentChangeCount = count
        currentChangeEnd = currentChangeStart + currentChangeCount
        val end = start + count
        val editorStart = editor.selectionStart
        if (editorStart == start + count) {
            if (count > 0) {
                val message = editor.editableText
                val list = message.getSpans(start, end, Any::class.java)
                synchronized(emoticonsToRemove) {
                    for (span in list) {
                        // Get only the emoticons that are inside of the changed
                        // region.
                        val spanStart = message.getSpanStart(span)
                        val spanEnd = message.getSpanEnd(span)
                        if (spanStart < end && spanEnd > start && span !is SpanWatcher) {
                            // Add to remove list
                            emoticonsToRemove.add(span)
                        }
                    }
                }
            }
        } else {
            emoticonsToRemove.clear()
        }
    }

    override fun afterTextChanged(text: Editable?) {
        val message = editor.editableText
        val editorStart = editor.selectionStart
        if (editorStart == currentChangeStart) {
            synchronized(emoticonsToRemove) {
                for (span in emoticonsToRemove) {
                    val start = message.getSpanStart(span)
                    val end = message.getSpanEnd(span)

                    // Remove the span
                    message.removeSpan(span)

                    // Remove the remaining emoticon text.
                    if (start != end) {
                        message.delete(start, end)
                    }
                    Log.d("Edit", "Remove Span")
                }
                emoticonsToRemove.clear()
            }
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}
}