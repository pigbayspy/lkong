package io.pig.lkong.ui.profile.thread

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
import io.pig.lkong.model.UserThreadModel
import io.pig.lkong.ui.adapter.UserThreadAdapter

class UserThreadsFragment : Fragment() {

    private lateinit var listAdapter: UserThreadAdapter

    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var threadsViewModel: UserThreadsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = requireArguments().getLong(DataContract.BUNDLE_USER_ID)
        val userName = requireArguments().getString(DataContract.BUNDLE_USER_NAME) ?: ""
        val userAvatar = requireArguments().getString(DataContract.BUNDLE_USER_AVATAR) ?: ""
        val viewModelFactory = UserThreadsViewModelFactory(userId)
        threadsViewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(UserThreadsViewModel::class.java)

        // set title
        val title = getString(R.string.format_threads_of, userName)
        requireActivity().title = title

        listAdapter = UserThreadAdapter(requireContext(), userId, userName, userAvatar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)

        threadsViewModel.threads.observe(viewLifecycleOwner) {
            refresh(it)
        }
        threadsViewModel.loading.observe(viewLifecycleOwner) {
            refreshLoading(it)
        }
        threadsViewModel.getThreads()

        val root = binding.root
        root.setOnRefreshListener {
            threadsViewModel.clear()
        }
        binding.simpleContentList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        return root
    }

    private fun refresh(threads: List<UserThreadModel>) {
        listAdapter.submitList(threads)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {

        fun newInstance(userId: Long, username: String, userAvatar: String): UserThreadsFragment {
            val fragment = UserThreadsFragment()
            fragment.arguments = bundleOf(
                DataContract.BUNDLE_USER_ID to userId,
                DataContract.BUNDLE_USER_NAME to username,
                DataContract.BUNDLE_USER_AVATAR to userAvatar
            )
            return fragment
        }
    }
}