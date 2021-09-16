package io.pig.lkong.ui.edit

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html.ImageGetter
import android.text.SpannableString
import androidx.core.content.res.ResourcesCompat
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.util.SlateUtil
import io.pig.ui.html.EmptyImageGetter
import io.pig.ui.html.HtmlTagHandler
import io.pig.ui.html.HtmlUtil
import io.pig.widget.html.ClickableImageSpan

class NewThreadActivity : AbstractPostActivity() {

    private val tagHandler = HtmlTagHandler()

    private var isEditMode = false

    private var forumId: Long = -1L
    private var forumName: String = ""
    private var postId: String = ""
    private var threadId: Long = -1L
    private var editTitle: String = ""
    private var editContent: String = ""
    private lateinit var editTextHandler: ImageEditTextHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(DataContract.BUNDLE_FORUM_ID)) {
            forumId = intent.getLongExtra(DataContract.BUNDLE_FORUM_ID, forumId)
            forumName = intent.getStringExtra(DataContract.BUNDLE_FORUM_NAME) ?: forumName
        } else if (intent.hasExtra(DataContract.BUNDLE_IS_EDIT_MODE)) {
            isEditMode = true
            editTitle = intent.getStringExtra(DataContract.BUNDLE_EDIT_TITLE) ?: editTitle
            editContent = intent.getStringExtra(DataContract.BUNDLE_EDIT_CONTENT) ?: editContent
            threadId = intent.getLongExtra(DataContract.BUNDLE_THREAD_ID, 0L)
            postId = intent.getStringExtra(DataContract.BUNDLE_POST_ID) ?: postId
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (isInEditMode()) {
            binding.newThreadEditTitle.setText(editTitle)
            val imageGetter: ImageGetter = EmptyImageGetter()
            val spannedText = SlateUtil.slateToHtml(editContent)
            val spannedHtml = HtmlUtil.htmlToSpanned(spannedText, imageGetter, tagHandler)
            val drawable: Drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.placeholder_loading, null)!!
            val spannableString =
                replaceImageSpan(drawable, postId, spannedHtml) as SpannableString
            binding.newThreadEditContent.append(spannableString)
            val imageSpanContainer = ImageSpanContainerImpl(binding.newThreadEditContent)
            val objects = spannableString.getSpans(
                0, spannableString.length,
                Any::class.java
            )
            for (spanObj in objects) {
                if (spanObj is ClickableImageSpan) {
                    spanObj.loadImage(imageSpanContainer)
                }
            }
        }
        editTextHandler = ImageEditTextHandler(binding.newThreadEditContent)
    }

    override fun sendData(title: String?, content: String) {
        TODO("Not yet implemented")
    }

    override fun getLogTag(): String {
        return TAG
    }

    override fun hasTitleField(): Boolean {
        return true
    }

    override fun getTitleString(): String {
        return if (isInEditMode()) {
            getString(R.string.button_edit)
        } else {
            forumName
        }
    }

    override fun isInEditMode(): Boolean {
        return isEditMode
    }

    companion object {
        private const val TAG = "NewThreadActivity"
    }
}