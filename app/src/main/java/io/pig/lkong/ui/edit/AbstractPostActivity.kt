package io.pig.lkong.ui.edit

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import io.pig.lkong.R
import io.pig.lkong.databinding.ActivityNewThreadBinding
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.util.TextSizeUtil
import io.pig.ui.common.getPrimaryColor
import io.pig.ui.common.getThemeKey
import io.pig.ui.common.toggleNightMode
import io.pig.ui.snakebar.SnakeBarType
import io.pig.ui.snakebar.showSnakeBar
import io.pig.widget.html.AsyncDrawableType
import io.pig.widget.html.AsyncTargetDrawable

abstract class AbstractPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewThreadBinding

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

    companion object {
        private const val SELECT_PICTURE = 1
    }
}