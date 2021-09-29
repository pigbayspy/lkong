package io.pig.lkong.ui.profile.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import io.pig.lkong.application.const.DataContract

class UserReplyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance(userId: Long): UserReplyFragment {
            val bundle = bundleOf(DataContract.BUNDLE_USER_ID to userId)
            val fragment = UserReplyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}