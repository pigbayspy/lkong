package io.pig.lkong.ui.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.databinding.FragmentHistoryBinding
import io.pig.lkong.model.HistoryModel
import io.pig.lkong.ui.adapter.HistoryAdapter

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 自定义 menu
        setHasOptionsMenu(true)

        // 初始化
        historyViewModel.histories.observe(viewLifecycleOwner) {
            this.refresh(it)
        }
        historyViewModel.getHistory()
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_history_evening) {
            // Todo
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refresh(history: List<HistoryModel>) {
        binding.recycleListHistory.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleListHistory.adapter = HistoryAdapter(requireContext(), history)
    }
}