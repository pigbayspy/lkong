package io.pig.lkong.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.pig.lkong.R
import io.pig.lkong.databinding.FragmentGalleryBinding
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY
import io.pig.lkong.preference.PrefConst.AVATAR_DOWNLOAD_POLICY_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.rx.event.AbstractEvent
import io.pig.lkong.rx.event.FavoriteChangeEvent
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
}