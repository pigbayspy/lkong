package io.pig.lkong.ui.edit

import android.os.Bundle
import io.pig.lkong.application.const.DataContract

class NewThreadActivity : AbstractPostActivity() {

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

    }

    companion object {
        private const val TAG = "NewThreadActivity"
    }
}