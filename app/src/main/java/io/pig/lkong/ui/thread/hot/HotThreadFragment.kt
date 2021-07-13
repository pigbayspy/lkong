package io.pig.lkong.ui.thread.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.databinding.FragmentHotThreadBinding
import io.pig.lkong.model.HotThreadModel
import io.pig.lkong.ui.adapter.HotThreadAdapter

/**
 * 热门
 */
class HotThreadFragment : Fragment() {

    private lateinit var hotThreadViewModel: HotThreadViewModel
    private lateinit var binding: FragmentHotThreadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hotThreadViewModel = ViewModelProvider(this).get(HotThreadViewModel::class.java)
        binding = FragmentHotThreadBinding.inflate(inflater, container, false)
        val root = binding.root
        hotThreadViewModel.getHotThreads()
        hotThreadViewModel.hotThreads.observe(viewLifecycleOwner) {
            refreshHotThread(it)
        }
        return root
    }

    private fun refreshHotThread(hotThreads: List<HotThreadModel>) {
        binding.recycleListHotThread.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleListHotThread.adapter =
            HotThreadAdapter(requireActivity(), hotThreads)
    }

    companion object {
        fun newInstance() = HotThreadFragment()
    }
}