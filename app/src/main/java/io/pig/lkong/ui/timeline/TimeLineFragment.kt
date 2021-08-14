package io.pig.lkong.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.databinding.FragmentTimeLineBinding
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.ui.adapter.TimelineAdapter
import io.pig.ui.common.getThemeKey

/**
 * 时间线
 */
class TimeLineFragment : Fragment() {

    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var binding: FragmentTimeLineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        timelineViewModel = ViewModelProvider(this).get(TimelineViewModel::class.java)
        binding = FragmentTimeLineBinding.inflate(inflater, container, false)
        val root = binding.root
        timelineViewModel.apply {
            timelines.observe(viewLifecycleOwner) {
                refreshTimeline(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        timelineViewModel.getTimeline()
        root.setOnRefreshListener {
            timelineViewModel.refresh()
        }
        return root
    }

    private fun refreshTimeline(timelines: List<TimelineModel>) {
        binding.recycleListTimeline.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleListTimeline.adapter =
            TimelineAdapter(requireContext(), getThemeKey(), timelines)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {
        fun newInstance() = TimeLineFragment()
    }
}