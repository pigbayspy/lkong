package io.pig.lkong.ui.notify

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import io.pig.lkong.R
import io.pig.lkong.databinding.ActivityNotifyBinding
import io.pig.lkong.ui.adapter.MainTabFragmentAdapter
import io.pig.lkong.ui.adapter.item.FragmentItem

class NotifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotifyBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabs()
    }

    private fun initTabs() {
        val tabs = binding.fragmentNotifyTab
        val pages = binding.fragmentNotifyPager
        // 设置缓存
        pages.offscreenPageLimit = 2
        tabs.setupWithViewPager(pages)
        // 初始化 fragment
        val noticeFragment = NoticeFragment.newInstance()
        val noticeItem = FragmentItem(noticeFragment, getString(R.string.drawer_item_mentions), null)
        val fragments = listOf(noticeItem)

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

    private fun setDrawerTitle(title: String) {
        binding.notifyToolbar.title = title
    }
}