package io.pig.lkong.ui.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityUserProfileBinding
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.getThemeKey

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private var uid: Long = INVALID_USER_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        // 获取参数
        savedInstanceState?.apply {
            uid = savedInstanceState.getLong(DataContract.BUNDLE_USER_ID, INVALID_USER_ID)
        }
    }

    private fun setUpToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            val drawable = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_arrow_backward,
                null
            )!!
            ThemeUtil.setTint(
                drawable,
                ThemeUtil.textColorPrimaryInverse(this, getThemeKey())
            )
            actionBar.setHomeAsUpIndicator(drawable)
        }
    }

    companion object {
        private const val INVALID_USER_ID = -1L
    }
}