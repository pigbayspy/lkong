package io.pig.lkong.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.databinding.LayoutSimpleRecycleBinding
import io.pig.lkong.model.ForumModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.ui.adapter.ForumListAdapter
import io.pig.ui.common.AbstractFragment

/**
 * 板块列表
 *
 * @author yinhang
 */
class ForumsFragment : AbstractFragment() {

    private val clickListener: (forum: ForumModel) -> Unit = {
        AppNavigation.openForumContentActivity(requireContext(), it)
    }

    private lateinit var showInGridPrefs: BoolPrefs
    private lateinit var binding: LayoutSimpleRecycleBinding
    private lateinit var forumsViewModel: ForumsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        forumsViewModel = ViewModelProvider(this).get(ForumsViewModel::class.java)
        binding = LayoutSimpleRecycleBinding.inflate(inflater, container, false)
        val root = binding.root
        forumsViewModel.apply {
            forums.observe(viewLifecycleOwner) {
                refreshForumList(it)
            }
            loading.observe(viewLifecycleOwner) {
                refreshLoading(it)
            }
        }
        forumsViewModel.getForums()
        root.setOnRefreshListener {
            forumsViewModel.refresh()
        }
        return root
    }

    private fun initConfig() {
        showInGridPrefs = Prefs.getBoolPrefs(FORUMS_IN_GRID, FORUMS_IN_GRID_VALUE)
    }

    private fun getLayoutManager(): GridLayoutManager {
        val res = if (showInGridPrefs.get()) {
            R.integer.fragment_forum_grid_count
        } else {
            R.integer.fragment_forum_list_column_count
        }
        val spanCount = resources.getInteger(res)
        return GridLayoutManager(requireActivity(), spanCount)
    }

    private fun refreshForumList(forums: List<ForumModel>) {
        binding.simpleContentList.layoutManager = getLayoutManager()
        binding.simpleContentList.adapter =
            ForumListAdapter(requireActivity(), showInGridPrefs.get(), clickListener, forums)
    }

    private fun refreshLoading(loading: Boolean) {
        binding.root.isRefreshing = loading
    }

    companion object {
        fun newInstance() = ForumsFragment()
    }
}