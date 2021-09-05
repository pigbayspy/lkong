package io.pig.lkong.ui.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityUserProfileBinding
import io.pig.lkong.model.UserModel
import io.pig.lkong.ui.profile.fans.FansFragment
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userProfileViewModel: UserProfileViewModel

    private var uid = INVALID_USER_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        // 获取参数
        uid = intent.getLongExtra(DataContract.BUNDLE_USER_ID, INVALID_USER_ID)

        setContentView(binding.root)

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)
        userProfileViewModel.user.observe(this) {
            refresh(it)
        }
        userProfileViewModel.getUserProfile(uid)
    }

    private fun refresh(user: UserModel) {
        // 设置头像
        val avatarSize = resources.getDimensionPixelSize(R.dimen.size_avatar_user_profile)
        ImageLoaderUtil.loadLkongAvatar(
            this,
            binding.profileImageAvatar,
            user.uid,
            user.avatar,
            avatarSize
        )
        binding.profileTextUserName.text = user.name
        val statsTextSize = resources.getDimensionPixelSize(R.dimen.text_size_caption)
        binding.profileTextFollowerCount.apply {
            text = getUserStatsText(
                user.followers,
                getString(R.string.text_profile_header_followers),
                statsTextSize
            )
            setOnClickListener {
                val fragment = FansFragment.newInstance(user.uid, user.name)
                switchFragment(fragment)
            }
        }
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

    private fun switchFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        private const val INVALID_USER_ID = -1L
    }
}