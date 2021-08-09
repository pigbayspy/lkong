package io.pig.lkong.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.pig.lkong.MainActivity
import io.pig.lkong.R
import io.pig.lkong.databinding.FragmentHomeBinding
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst
import io.pig.lkong.preference.Prefs
import io.pig.lkong.ui.adapter.MainTabFragmentAdapter
import io.pig.lkong.ui.adapter.item.FragmentItem
import io.pig.lkong.ui.forum.ForumsFragment
import io.pig.lkong.ui.forum.follow.FollowForumsFragment
import io.pig.lkong.ui.thread.digest.DigestThreadFragment
import io.pig.lkong.ui.thread.hot.HotThreadFragment
import io.pig.lkong.ui.timeline.TimeLineFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    private lateinit var pages: ViewPager
    private lateinit var tabs: TabLayout

    private lateinit var forumsFirst: BoolPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initConfig()
        val root: View = binding.root
        initTabs(inflater, container)
        return root
    }

    override fun onResume() {
        super.onResume()
        attachTab()
    }

    override fun onPause() {
        super.onPause()
        detachTab()
    }

    private fun initConfig() {
        // 初始化界面设置
        forumsFirst = Prefs.getBoolPrefs(
            PrefConst.FORUMS_FIRST,
            PrefConst.FORUMS_FIRST_VALUE
        )
    }

    private fun initTabs(inflater: LayoutInflater, container: ViewGroup?) {
        // 初始化 tab
        tabs = inflater.inflate(R.layout.layout_tab, container, false) as TabLayout
        pages = binding.fragmentContentMainPager
        tabs.setupWithViewPager(pages)
        setupViewPager()
    }

    private fun attachTab() {
        val activity = requireActivity() as MainActivity
        activity.addAppBarView(tabs)
    }

    private fun detachTab() {
        val activity = requireActivity() as MainActivity
        activity.removeAppBarView(tabs)
    }

    private fun setupViewPager() {
        val activity = requireActivity() as MainActivity
        val forumsFragment = ForumsFragment.newInstance()
        val followForumsFragment = FollowForumsFragment.newInstance()
        val timelineFragment = TimeLineFragment.newInstance()
        val hotThreadFragment = HotThreadFragment.newInstance()
        val digestThreadFragment = DigestThreadFragment.newInstance()
        val forumsItem = FragmentItem(
            forumsFragment,
            getString(R.string.tab_item_forum),
            R.drawable.ic_tab_forums
        )
        val followForumsItem = FragmentItem(
            followForumsFragment,
            getString(R.string.tab_item_follow_forum),
            R.drawable.ic_tab_stared
        )
        val timeLineItem = FragmentItem(
            timelineFragment,
            getString(R.string.tab_item_timeline),
            R.drawable.ic_tab_timeline
        )
        val hotThreadItem = FragmentItem(
            hotThreadFragment,
            getString(R.string.tab_item_hot_thread),
            R.drawable.ic_tab_whatshot
        )
        val digestThreadItem = FragmentItem(
            digestThreadFragment,
            getString(R.string.tab_item_digest_thread),
            R.drawable.ic_tab_digest
        )
        val fragments = if (forumsFirst.get()) {
            listOf(forumsItem, followForumsItem, timeLineItem)
        } else {
            listOf(timeLineItem, followForumsItem, forumsItem)
        } + listOf(hotThreadItem, digestThreadItem)

        val fragmentAdapter = MainTabFragmentAdapter(childFragmentManager, fragments)
        pages.adapter = fragmentAdapter
        for (i in 0..tabs.tabCount) {
            val tab = tabs.getTabAt(i)
            tab?.apply {
                setIcon(fragmentAdapter.getIcon(i))
                text = ""
            }
        }
        pages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                activity.setDrawerTitle(fragmentAdapter.getPageTitle(position))
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }
}