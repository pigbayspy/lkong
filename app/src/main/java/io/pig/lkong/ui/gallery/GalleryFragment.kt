package io.pig.lkong.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.pig.lkong.R
import io.pig.lkong.databinding.FragmentGalleryBinding
import io.pig.lkong.model.ThreadModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.rx.event.AbstractEvent
import io.pig.lkong.rx.event.FavoriteChangeEvent
import io.pig.lkong.ui.adapter.ThreadListAdapter
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.ui.common.Eventable


class GalleryFragment : Eventable, Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var selfBinding: FragmentGalleryBinding
    private lateinit var avatarDownloadPolicy: StringPrefs

    private var needReload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 载入设置
        avatarDownloadPolicy =
            Prefs.getStringPrefs(AVATAR_DOWNLOAD_POLICY, AVATAR_DOWNLOAD_POLICY_VALUE)
        // 自定义的menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        selfBinding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = selfBinding.root
        // 初始化监听器
        galleryViewModel.threadList.observe(viewLifecycleOwner, {
            refreshThreadList(it)
        })
        // 初始化数据
        galleryViewModel.getThreads()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val v = view
        if (v != null) {
            v.isFocusableInTouchMode = true
            v.requestFocus()
        }
    }

    override fun onResume() {
        super.onResume()
        if (needReload) {
            needReload = false
        }
    }

    override fun onEvent(event: AbstractEvent) {
        if (event is FavoriteChangeEvent) {
            needReload = true
        }
    }

    private fun refreshThreadList(threads: List<ThreadModel>) {
        val listener = object : OnThreadClickListener {
            private fun checkInvalid(pos: Int): Boolean {
                if (pos < 0 && pos >= threads.size) {
                    return true
                }
                return false
            }

            override fun onItemThreadClick(view: View, pos: Int) {
                if (checkInvalid(pos)) {
                    val thread = threads[pos]
                    AppNavigation.openActivityForPostListByThreadId(
                        requireContext(),
                        thread.idNum()
                    )
                }
            }

            override fun onProfileAreaClick(view: View, pos: Int, uid: Long) {
                if (checkInvalid(pos)) {
                    val thread = threads[pos]
                    val startLocation = intArrayOf(view.width / 2, 0)
                    view.getLocationOnScreen(startLocation)
                    AppNavigation.openActivityForUserProfile(
                        requireActivity(),
                        startLocation,
                        thread.userId
                    )
                }
            }
        }
        selfBinding.recycleListGallery.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        selfBinding.recycleListGallery.adapter =
            ThreadListAdapter(requireActivity(), listener, threads)
    }
}