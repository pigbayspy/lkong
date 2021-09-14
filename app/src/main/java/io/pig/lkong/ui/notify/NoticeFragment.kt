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
import io.pig.lkong.model.NoticeModel
import io.pig.lkong.ui.adapter.NoticeAdapter
import io.pig.ui.common.getThemeKey

class NoticeFragment : Fragment() {

    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var noticeViewModel: NoticeViewModel

    private val listAdapter by lazy { NoticeAdapter(requireContext(), getThemeKey()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeViewModel = ViewModelProvider(this).get(NoticeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        noticeViewModel.apply {
            notices.observe(viewLifecycleOwner) {
                refreshTimeline(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        noticeViewModel.getMentions()
        root.setOnRefreshListener {
            noticeViewModel.refresh()
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
                        noticeViewModel.getMentions()
                    }
                }
            })
        }
        return root
    }

    private fun refreshTimeline(noticeList: List<NoticeModel>) {
        listAdapter.submitList(noticeList)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {
        private const val TO_LAST_LEFT = 5

        fun newInstance(): NoticeFragment {
            return NoticeFragment()
        }
    }
}