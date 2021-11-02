package io.pig.lkong.ui.edit

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import io.pig.lkong.R
import io.pig.lkong.databinding.ActivityNewThreadBinding
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.util.TextSizeUtil
import io.pig.ui.common.AbstractActivity
import io.pig.ui.snakebar.SnakeBarType
import io.pig.widget.html.AsyncDrawableType
import io.pig.widget.html.AsyncTargetDrawable
import io.pig.widget.html.ClickableImageSpan
import io.pig.widget.html.EmojiSpan

abstract class AbstractPostActivity : AbstractActivity() {

    protected lateinit var binding: ActivityNewThreadBinding

    private lateinit var postTailText: StringPrefs

    private var contentTextSize: Float = 0f
    private var outputFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )
        binding = ActivityNewThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initConfig()
        initToolbar()

        contentTextSize =
            TextSizeUtil.textSizeForMode(this, getThemeKey(), TextSizeUtil.TEXT_SIZE_BODY).toFloat()
        binding.newThreadEditContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize)
        binding.newThreadEditTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize)
        binding.newThreadEditTitle.visibility = if (hasTitleField()) View.VISIBLE else View.GONE
        binding.newThreadViewDiv.visibility = if (hasTitleField()) View.VISIBLE else View.GONE

        title = getTitleString()
    }

    private fun initToolbar() {
        val toolbar = binding.newThreadToolbar
        toolbar.setBackgroundColor(getPrimaryColor())
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_post -> {
                submitPost()
                return true
            }
            R.id.action_change_theme -> {
                toggleNightMode()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitPost() {
        val titleEdit = binding.newThreadEditTitle
        val contentEdit = binding.newThreadEditContent
        titleEdit.clearFocus()
        contentEdit.clearFocus()
        val title: String = titleEdit.text.toString()
        val spannableContent: Editable = contentEdit.text
        if (!TextUtils.isEmpty(spannableContent)) {
            if (hasTitleField() && TextUtils.isEmpty(title)) {
                showSnakeBar(
                    binding.root, getString(R.string.toast_error_title_empty),
                    SnakeBarType.ERROR
                )
                return
            }
            val progressDialog =
                ProgressDialog.show(this, "", getString(R.string.dialog_new_post_sending))
            progressDialog.setCancelable(true)
            progressDialog.setCanceledOnTouchOutside(false)
            val sendContentBuilder = StringBuilder()
            if (!isInEditMode()) {
                // Todo
            }
            sendData((if (hasTitleField()) title else null), sendContentBuilder.toString())
        } else if (hasTitleField() && TextUtils.isEmpty(title)) {
            showSnakeBar(
                binding.root,
                getString(R.string.toast_error_title_empty),
                SnakeBarType.ERROR
            )
        } else {
            showSnakeBar(
                binding.root,
                getString(R.string.toast_error_content_empty),
                SnakeBarType.ERROR
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val isCamera = if (data == null) {
                    true
                } else {
                    val action = data.action
                    if (action == null) {
                        false
                    } else {
                        action == MediaStore.ACTION_IMAGE_CAPTURE
                    }
                }
                val selectedImageUri: Uri? = if (isCamera) {
                    outputFileUri
                } else {
                    data?.data
                }
                val drawable = AsyncTargetDrawable(
                    this,
                    null,
                    getLogTag(),
                    AsyncDrawableType.NORMAL,
                    ResourcesCompat.getDrawable(resources, R.drawable.placeholder_loading, theme),
                    (contentTextSize * 4).toInt(),
                    (contentTextSize * 4).toInt()
                )
                Glide
                    .with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.placeholder_loading)
                    .error(R.drawable.placeholder_error)
                    .override((contentTextSize * 4).toInt(), (contentTextSize * 4).toInt())
                    .centerCrop()
                    .into(drawable)

            }
        }
    }

    private fun initConfig() {
        postTailText = Prefs.getStringPrefs(
            PrefConst.POST_TAIL_TEXT,
            PrefConst.POST_TAIL_TEXT_VALUE
        )
    }

    abstract fun hasTitleField(): Boolean

    abstract fun getTitleString(): String

    abstract fun isInEditMode(): Boolean

    abstract fun sendData(title: String?, content: String)

    abstract fun getLogTag(): String

    protected fun replaceImageSpan(
        initPlaceHolder: Drawable?,
        pid: String,
        sequence: CharSequence
    ): CharSequence {
        val spannable: Spannable = SpannableString(sequence)
        val imageSpans = spannable.getSpans(
            0, sequence.length,
            ImageSpan::class.java
        )
        for (imageSpan in imageSpans) {
            val spanStart = spannable.getSpanStart(imageSpan)
            val spanEnd = spannable.getSpanEnd(imageSpan)
            val spanFlags = spannable.getSpanFlags(imageSpan)
            if (!TextUtils.isEmpty(imageSpan.source) && !imageSpan.source!!.contains("http://img.lkong.cn/bq/")) {
                spannable.removeSpan(imageSpan)
                val clickableImageSpan = ClickableImageSpan(
                    this,
                    null,
                    pid,
                    PICASSO_TAG,
                    imageSpan.source!!,
                    R.drawable.placeholder_loading,
                    R.drawable.placeholder_error,
                    256,
                    256,
                    DynamicDrawableSpan.ALIGN_BASELINE,
                    initPlaceHolder!!
                )
                spannable.setSpan(
                    clickableImageSpan,
                    spanStart,
                    spanEnd,
                    spanFlags
                )
            } else if (!TextUtils.isEmpty(imageSpan.source) && imageSpan.source!!.contains("http://img.lkong.cn/bq/")) {
                spannable.removeSpan(imageSpan)
                val emoticonImageSpan = EmojiSpan(
                    this,
                    imageSpan.source!!,
                    (contentTextSize * 2 * 2).toInt(),
                    ImageSpan.ALIGN_BASELINE,
                    contentTextSize.toInt() * 2
                )
                spannable.setSpan(
                    emoticonImageSpan,
                    spanStart,
                    spanEnd,
                    spanFlags
                )
            }
        }
        return spannable
    }

    companion object {
        private const val SELECT_PICTURE = 1

        private const val PICASSO_TAG = "abstract_post_activity"
    }
}