package io.pig.lkong.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.databinding.FragmentTimeLineBinding
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.TimelineAdapter
import io.pig.lkong.ui.adapter.listener.OnTimelineClickListener
import io.pig.ui.common.getThemeKey

/**
 * 时间线
 */
class TimeLineFragment : Fragment() {
    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var binding: FragmentTimeLineBinding

    private val listener = object : OnTimelineClickListener {
        override fun onItemTimelineClick(view: View, timeline: TimelineModel) {
            TODO("Not yet implemented")
        }

        override fun onProfileAreaClick(view: View, uid: Long) {
            AppNavigation.openUserProfileActivity(requireActivity(), uid)
        }
    }

    private val listAdapter by lazy { TimelineAdapter(requireContext(), listener, getThemeKey()) }

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
        val layoutMgr = LinearLayoutManager(requireContext())
        binding.recycleListTimeline.apply {
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