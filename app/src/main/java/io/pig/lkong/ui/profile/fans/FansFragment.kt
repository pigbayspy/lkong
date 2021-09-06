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
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.FansModel
import io.pig.lkong.ui.adapter.FansAdapter


class FansFragment : Fragment() {

    private val listAdapter by lazy { FansAdapter(requireContext()) }

    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var fansViewModel: FansViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId = requireArguments().getLong(DataContract.BUNDLE_USER_ID)
        val userName = requireArguments().getString(DataContract.BUNDLE_USER_NAME)
        val viewModelFactory = FansViewModelFactory(userId)
        fansViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(FansViewModel::class.java)
        // set title
        val title = getString(R.string.format_followers_of, userName)
        requireActivity().title = title
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)

        fansViewModel.fans.observe(viewLifecycleOwner) {
            refresh(it)
        }
        fansViewModel.loading.observe(viewLifecycleOwner) {
            refreshLoading(it)
        }
        fansViewModel.getFans()

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

        fun newInstance(userId: Long, username: String) =
            FansFragment().apply {
                arguments = Bundle().apply {
                    putLong(DataContract.BUNDLE_USER_ID, userId)
                    putString(DataContract.BUNDLE_USER_NAME, username)
                }
            }
    }
}