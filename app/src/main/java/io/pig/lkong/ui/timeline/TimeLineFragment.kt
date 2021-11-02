package io.pig.lkong.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.TimelineAdapter
import io.pig.lkong.ui.adapter.listener.OnTimelineClickListener
import io.pig.ui.common.AbstractFragment

/**
 * 时间线
 */
class TimeLineFragment : AbstractFragment() {
    private lateinit var timelineViewModel: TimelineViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        timelineViewModel = ViewModelProvider(this).get(TimelineViewModel::class.java)
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
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
        val layoutMgr = LinearLayoutManager(requireContext())
        binding.simpleContentList.apply {
            layoutManager = layoutMgr
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPos = layoutMgr.findLastCompletelyVisibleItemPosition()
                    if (dy > 0 && layoutMgr.itemCount - lastPos <= TO_LAST_LEFT) {
                        // load more
                        timelineViewModel.getTimeline()
                    }
                }
            })
        }

        return root
    }

    private fun refreshTimeline(timelines: List<TimelineModel>) {
        listAdapter.submitList(timelines)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {

        private const val TO_LAST_LEFT = 5

        fun newInstance() = TimeLineFragment()
    }
}