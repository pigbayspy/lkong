package io.pig.lkong.ui.profile.fans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import io.pig.lkong.R
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.FansModel
import io.pig.lkong.ui.adapter.FansAdapter
import io.pig.ui.common.AbstractFragment


class FansFragment : AbstractFragment() {

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
        val title = getString(R.string.format_fans_of, userName)
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
                arguments = bundleOf(
                    DataContract.BUNDLE_USER_ID to userId,
                    DataContract.BUNDLE_USER_NAME to username
                )
            }
    }
}
