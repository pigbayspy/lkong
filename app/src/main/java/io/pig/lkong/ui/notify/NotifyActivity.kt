package io.pig.lkong.ui.notify

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import io.pig.lkong.R
import io.pig.lkong.databinding.ActivityNotifyBinding
import io.pig.lkong.ui.adapter.MainTabFragmentAdapter
import io.pig.lkong.ui.adapter.item.FragmentItem
import io.pig.ui.common.AbstractActivity

class NotifyActivity : AbstractActivity() {

    private lateinit var binding: ActivityNotifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()
        initToolbar()
    }

    private fun initTabs() {
        val tabs = binding.fragmentNotifyTab
        tabs.setBackgroundColor(getPrimaryColor())
        val pages = binding.fragmentNotifyPager
        // 设置缓存
        tabs.setupWithViewPager(pages)
        // 初始化 fragment
        val noticeFragment = NoticeFragment.newInstance()
        val noticeItem =
            FragmentItem(noticeFragment, getString(R.string.drawer_item_notice), -1)
        val mentionFragment = MentionFragment.newInstance()
        val mentionItem =
            FragmentItem(mentionFragment, getString(R.string.drawer_item_mentions), -1)
        val pmFragment = PmFragment.newInstance()
        val pmItem = FragmentItem(pmFragment, getString(R.string.drawer_item_private_messages), -1)
        val fragments = listOf(noticeItem, mentionItem, pmItem)
        pages.offscreenPageLimit = fragments.size - 1
        val fragmentAdapter = MainTabFragmentAdapter(supportFragmentManager, fragments)
        pages.adapter = fragmentAdapter
        for (i in 0..tabs.tabCount) {
            val tab = tabs.getTabAt(i)
            tab?.apply {
                text = fragmentAdapter.getPageTitle(i)
            }
        }
        pages.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageSelected(position: Int) {
                setDrawerTitle(fragmentAdapter.getPageTitle(position))
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })
    }

    private fun initToolbar() {
        binding.notifyToolbar.setBackgroundColor(getPrimaryColor())
        setSupportActionBar(binding.notifyToolbar)
    }

    private fun setDrawerTitle(title: String) {
        binding.notifyToolbar.title = title
    }
}