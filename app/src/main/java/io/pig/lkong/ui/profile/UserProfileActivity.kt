package io.pig.lkong.ui.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityUserProfileBinding
import io.pig.lkong.model.UserModel
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.LkongUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.getThemeKey

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userProfileViewModel: UserProfileViewModel

    private var uid = INVALID_USER_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        // 获取参数
        savedInstanceState?.let {
            uid = savedInstanceState.getLong(DataContract.BUNDLE_USER_ID, INVALID_USER_ID)
        }

        setContentView(binding.root)

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        userProfileViewModel.user.observe(this) {
            refresh(it)
        }
        userProfileViewModel.getUserProfile()
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

    private fun refresh(user: UserModel) {
        // 设置头像
        val avatarSize = resources.getDimensionPixelSize(R.dimen.size_avatar_user_profile)
        val avatarUrl = LkongUtil.generateAvatarUrl(user.uid, user.avatar)
        ImageLoaderUtil.loadLkongAvatar(
            this,
            binding.profileImageAvatar,
            avatarUrl,
            avatarSize
        )
        binding.profileTextUserName.text = user.name
        val statsTextSize = resources.getDimensionPixelSize(R.dimen.text_size_caption)
        binding.profileTextFollowerCount.text = getUserStatsText(
            user.followers,
            getString(R.string.text_profile_header_followers),
            statsTextSize
        )
        binding.profileTextFollowingCount.text = getUserStatsText(
            user.followings,
            getString(R.string.text_profile_header_following),
            statsTextSize
        )
        binding.profileTextThreadCount.text = getUserStatsText(
            user.threads,
            getString(R.string.text_profile_header_threads),
            statsTextSize
        )
        binding.profileTextPostCount.text = getUserStatsText(
            user.posts,
            getString(R.string.text_profile_header_posts),
            statsTextSize
        )
        binding.profileTextCoin.text = user.money.toString()
        binding.profileTextDiamond.text = user.diamond.toString()
        binding.profileTextTotalPunch.text = user.punchAllDay.toString()
        binding.profileTextCurrentPunch.text = user.punchDay.toString()
        binding.profileTextLongestPunch.text = user.punchHighestDay.toString()
        binding.profileTextRegistTime.text = DateUtil.formatDateByTimestamp(user.dateline)
    }

    private fun getUserStatsText(
        value: Long,
        describeText: String,
        describeSize: Int
    ): CharSequence {
        val valueString = value.toString()
        val builder = SpannableStringBuilder()
        builder.append(valueString).append("\n")
        val start = builder.length
        val end = start + describeText.length
        builder.append(describeText)
        builder.setSpan(
            AbsoluteSizeSpan(describeSize),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    companion object {
        private const val INVALID_USER_ID = -1L
    }
}