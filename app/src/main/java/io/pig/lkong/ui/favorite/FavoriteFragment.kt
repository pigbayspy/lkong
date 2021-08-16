package io.pig.lkong.ui.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.databinding.FragmentFavoriteBinding
import io.pig.lkong.model.FavoriteThreadModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.ui.adapter.FavoriteAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.ui.common.Injectable
import javax.inject.Inject


class FavoriteFragment : Fragment(), Injectable {

    private val listener = object : OnThreadClickListener {
        override fun onItemThreadClick(view: View, tid: Long) {
            AppNavigation.openActivityForPostListByThreadId(requireContext(), tid)
        }

        override fun onProfileAreaClick(view: View, uid: Long) {
            AppNavigation.openActivityForUserProfile(requireActivity(), uid)
        }
    }

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var avatarDownloadPolicy: StringPrefs

    @Inject
    lateinit var userAccountMgr: UserAccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 载入设置
        avatarDownloadPolicy =
            Prefs.getStringPrefs(AVATAR_DOWNLOAD_POLICY, AVATAR_DOWNLOAD_POLICY_VALUE)
        // 自动注入
        injectThis()
        // 自定义的menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoriteViewModel =
            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root
        // 初始化监听器
        favoriteViewModel.threadList.observe(viewLifecycleOwner, {
            refreshThreadList(it)
        })
        // 初始化数据
        favoriteViewModel.getThreads(userAccountMgr.getCurrentUserAccount().userId)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.gallery_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_gallery_evening) {
            // Todo
        }
        return super.onOptionsItemSelected(item)
    }

    override fun injectThis() {
        LkongApplication.get(requireContext()).presentComponent().inject(this)
    }

    private fun refreshThreadList(threads: List<FavoriteThreadModel>) {
        binding.recycleListGallery.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recycleListGallery.adapter =
            FavoriteAdapter(requireActivity(), listener, threads)
    }
}