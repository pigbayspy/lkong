package io.pig.lkong.ui.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.ActivityUserProfileBinding
import io.pig.lkong.model.UserModel
import io.pig.lkong.ui.profile.fans.FansFragment
import io.pig.lkong.ui.profile.followers.FollowersFragment
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil

class UserProfileFragment : Fragment() {

    private val avatarSize by lazy { resources.getDimensionPixelSize(R.dimen.size_avatar_user_profile) }

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = requireArguments().getLong(DataContract.BUNDLE_USER_ID)
        val viewModelFactory = UserProfileViewModelFactory(userId)
        userProfileViewModel =
            ViewModelProvider(this, viewModelFactory).get(UserProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        userProfileViewModel.user.observe(viewLifecycleOwner) {
            refresh(it)
        }
        userProfileViewModel.getUserProfile()
        return binding.root
    }

    private fun refresh(user: UserModel) {
        // get activity
        val activity = requireActivity() as UserProfileActivity
        // 设置头像
        ImageLoaderUtil.loadLkongAvatar(
            requireContext(),
            binding.profileImageAvatar,
            user.uid,
            user.avatar,
            avatarSize
        )
        binding.profileTextUserName.text = user.name
        val statsTextSize = resources.getDimensionPixelSize(R.dimen.text_size_caption)
        binding.profileTextFansCount.apply {
            text = getUserStatsText(
                user.fans,
                getString(R.string.text_profile_header_followers),
                statsTextSize
            )
            setOnClickListener {
                val fragment = FansFragment.newInstance(user.uid, user.name)
                activity.switchFragment(fragment)
            }
        }
        binding.profileTextFollowersCount.apply {
            text = getUserStatsText(
                user.followers,
                getString(R.string.text_profile_header_following),
                statsTextSize
            )
            setOnClickListener {
                val fragment = FollowersFragment.newInstance(user.uid, user.name)
                activity.switchFragment(fragment)
            }
        }
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
        binding.profileTextRegisterTime.text = DateUtil.formatDateByTimestamp(user.dateline)
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
        fun newInstance(args: Bundle?): UserProfileFragment {
            val fragment = UserProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }
}