package io.pig.lkong.ui.profile.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.TimelineAdapter
import io.pig.lkong.ui.adapter.listener.OnTimelineClickListener
import io.pig.ui.common.getThemeKey

class UserReplyFragment : Fragment() {

    private lateinit var replyViewModel: ReplyViewModel
    private lateinit var binding: LayoutSimpleRecycleBinding

    private val listener = object : OnTimelineClickListener {
        override fun onItemTimelineClick(view: View, timeline: TimelineModel) {
            AppNavigation.openPostListActivity(requireContext(), timeline.threadId)
        }

        override fun onProfileAreaClick(view: View, uid: Long) {
            AppNavigation.openUserProfileActivity(requireContext(), uid)
        }
    }

    private val listAdapter by lazy { TimelineAdapter(requireContext(), listener, getThemeKey()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = requireArguments().getLong(DataContract.BUNDLE_USER_ID)
        val userName = requireArguments().getString(DataContract.BUNDLE_USER_NAME)
        val viewModelFactory = ReplyViewModelFactory(userId)
        replyViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ReplyViewModel::class.java)
        // set title
        val title = getString(R.string.format_replies_of, userName)
        requireActivity().title = title
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)

        replyViewModel.replies.observe(viewLifecycleOwner) {
            refresh(it)
        }
        replyViewModel.loading.observe(viewLifecycleOwner) {
            refreshLoading(it)
        }
        replyViewModel.getReplies()

        val root = binding.root
        root.setOnRefreshListener {
            replyViewModel.clear()
        }
        binding.simpleContentList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        return root
    }

    private fun refresh(timelines: List<TimelineModel>) {
        listAdapter.submitList(timelines)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }


    companion object {
        fun newInstance(userId: Long, username: String): UserReplyFragment {
            val bundle = bundleOf(
                DataContract.BUNDLE_USER_ID to userId,
                DataContract.BUNDLE_USER_NAME to username
            )
            val fragment = UserReplyFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}