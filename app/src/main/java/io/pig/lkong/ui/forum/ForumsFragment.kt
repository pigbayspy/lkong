package io.pig.lkong.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.databinding.FragmentForumsBinding
import io.pig.lkong.model.ForumModel
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.ui.adapter.ForumListAdapter

/**
 * 板块列表
 *
 * @author yinhang
 */
class ForumsFragment : Fragment() {

    private lateinit var showInGridPrefs: BoolPrefs
    private lateinit var selfBinding: FragmentForumsBinding
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
        selfBinding = FragmentForumsBinding.inflate(inflater, container, false)
        val view = selfBinding.root
        forumsViewModel.forums.observe(viewLifecycleOwner) {
            refreshForumList(it)
        }
        forumsViewModel.getForums()
        return view
    }

    private fun initConfig() {
        showInGridPrefs = Prefs.getBoolPrefs(FORUMS_IN_GRID, FORUMS_IN_GRID_VALUE)
    }

    private fun refreshForumList(forums: List<ForumModel>) {
        selfBinding.recycleListForum.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        selfBinding.recycleListForum.adapter =
            ForumListAdapter(requireActivity(), showInGridPrefs.get(), forums)
    }

    companion object {
        fun newInstance() = ForumsFragment()
    }
}