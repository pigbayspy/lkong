package io.pig.lkong.ui.thread.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.HotThreadModel
import io.pig.lkong.model.listener.OnItemThreadClickListener
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.HotThreadAdapter
import io.pig.ui.common.AbstractFragment

/**
 * 热门
 */
class HotThreadFragment : AbstractFragment() {

    private val listener = object : OnItemThreadClickListener {
        override fun onItemThreadClick(view: View, tid: Long) {
            AppNavigation.openPostListActivity(requireContext(), tid)
        }
    }

    private lateinit var hotThreadViewModel: HotThreadViewModel
    private lateinit var binding: LayoutSimpleRecycleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hotThreadViewModel = ViewModelProvider(this).get(HotThreadViewModel::class.java)
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        hotThreadViewModel.apply {
            hotThreads.observe(viewLifecycleOwner) {
                refreshHotThread(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        hotThreadViewModel.getHotThreads()
        root.setOnRefreshListener {
            hotThreadViewModel.refresh()
        }
        return root
    }

    private fun refreshHotThread(hotThreads: List<HotThreadModel>) {
        binding.simpleContentList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.simpleContentList.adapter =
            HotThreadAdapter(requireActivity(), getThemeKey(), listener, hotThreads)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {
        fun newInstance() = HotThreadFragment()
    }
}