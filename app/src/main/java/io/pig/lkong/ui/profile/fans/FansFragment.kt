package io.pig.lkong.ui.profile.fans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import io.pig.lkong.R
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.FansModel
import io.pig.lkong.ui.adapter.FansAdapter


class FansFragment : Fragment() {

    private lateinit var fansViewModel: FansViewModel

    private val listAdapter by lazy { FansAdapter(requireContext()) }

    private lateinit var binding: LayoutSimpleRecycleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = requireArguments().getLong(ARG_USER_ID)
        val userName = requireArguments().getString(ARG_USER_NAME)
        val viewModelFactory = FansViewModelFactory(userId)

        // set title
        val title = getString(R.string.format_followers_of, userName)
        requireActivity().title = title

        fansViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(FansViewModel::class.java)
        fansViewModel.fans.observe(viewLifecycleOwner) {
            refresh(it)
        }
        fansViewModel.loading.observe(viewLifecycleOwner) {
            refreshLoading(it)
        }
        fansViewModel.getFans()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        root.setOnRefreshListener {
            fansViewModel.clear()
        }
        binding.simpleContentList.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        return root
    }

    private fun refresh(fans: List<FansModel>) {
        listAdapter.submitList(fans)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {
        private const val ARG_USER_ID = "user_id"
        private const val ARG_USER_NAME = "user_name"

        fun newInstance(userId: Long, username: String) =
            FansFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, userId)
                    putString(ARG_USER_NAME, username)
                }
            }
    }
}