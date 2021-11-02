package io.pig.lkong.ui.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.PmUserModel
import io.pig.lkong.ui.adapter.PmUserAdapter
import io.pig.ui.common.AbstractFragment

class PmFragment : AbstractFragment() {

    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var pmViewModel: PmViewModel

    private val listAdapter by lazy { PmUserAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pmViewModel = ViewModelProvider(this).get(PmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        pmViewModel.apply {
            pmUsers.observe(viewLifecycleOwner) {
                refreshPmUsers(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        pmViewModel.getPmUsers()
        root.setOnRefreshListener {
            pmViewModel.refresh()
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
                        pmViewModel.getPmUsers()
                    }
                }
            })
        }
        return root
    }

    private fun refreshPmUsers(pmUsers: List<PmUserModel>) {
        listAdapter.submitList(pmUsers)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {

        private const val TO_LAST_LEFT = 5

        fun newInstance() = PmFragment()
    }
}