package io.pig.lkong.ui.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.TimelineAdapter
import io.pig.lkong.ui.adapter.listener.OnTimelineClickListener
import io.pig.ui.common.getThemeKey

class MentionFragment : Fragment() {

    private lateinit var mentionViewModel: MentionViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mentionViewModel = ViewModelProvider(this).get(MentionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        mentionViewModel.apply {
            mentions.observe(viewLifecycleOwner) {
                refreshTimeline(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        mentionViewModel.getMentions()
        root.setOnRefreshListener {
            mentionViewModel.refresh()
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
                        mentionViewModel.getMentions()
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

        fun newInstance(): MentionFragment {
            return MentionFragment()
        }
    }
}