package io.pig.lkong.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.model.ForumModel
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID
import io.pig.lkong.preference.PrefConst.FORUMS_IN_GRID_VALUE
import io.pig.lkong.preference.Prefs

/**
 * 板块列表
 *
 * @author yinhang
 */
class ForumsFragment : Fragment() {

    private lateinit var showInGridPrefs: BoolPrefs

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
        forumsViewModel.forums.observe(viewLifecycleOwner) {
            refreshForumList(it)
        }
        forumsViewModel.getForums()
        return inflater.inflate(R.layout.fragment_forums, container, false)
    }

    private fun initConfig() {
        showInGridPrefs = Prefs.getBoolPrefs(FORUMS_IN_GRID, FORUMS_IN_GRID_VALUE)
    }

    private fun refreshForumList(forums: List<ForumModel>) {

    }

    companion object {
        fun newInstance() = ForumsFragment()
    }
}