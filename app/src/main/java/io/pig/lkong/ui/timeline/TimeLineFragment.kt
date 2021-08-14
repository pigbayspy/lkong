package io.pig.lkong.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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

    private val layoutMgr = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

    private val listAdapter by lazy { TimelineAdapter(requireContext(), getThemeKey()) }

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
        binding.recycleListTimeline.apply {
            layoutManager = layoutMgr
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val into = layoutMgr.findLastCompletelyVisibleItemPositions(null)
                    val lastPos = into.maxOrNull() ?: 1

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