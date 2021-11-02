package io.pig.lkong.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.databinding.FragmentHistoryBinding
import io.pig.lkong.model.HistoryModel
import io.pig.lkong.model.listener.OnItemThreadClickListener
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.HistoryAdapter
import io.pig.lkong.ui.common.Injectable
import io.pig.ui.common.AbstractFragment
import javax.inject.Inject

class HistoryFragment : AbstractFragment(), Injectable {

    private val listener = object : OnItemThreadClickListener {
        override fun onItemThreadClick(view: View, tid: Long) {
            AppNavigation.openPostListActivity(requireContext(), tid)
        }
    }

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding

    @Inject
    lateinit var database: LkongDatabase

    @Inject
    lateinit var userAccountManager: UserAccountManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // 依赖注入
        injectThis()

        val root: View = binding.root

        // 自定义 menu
        setHasOptionsMenu(true)

        // 初始化
        historyViewModel.histories.observe(viewLifecycleOwner) {
            this.refresh(it)
        }
        val uid = userAccountManager.getCurrentUserAccount().userId
        historyViewModel.getHistory(database, uid)
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
        binding.recycleListHistory.adapter = HistoryAdapter(requireContext(), listener, history)
    }

    override fun injectThis() {
        LkongApplication.get(requireContext()).presentComponent().inject(this)
    }
}