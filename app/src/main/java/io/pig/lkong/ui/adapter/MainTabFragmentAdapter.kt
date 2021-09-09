package io.pig.lkong.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.pig.lkong.ui.adapter.item.FragmentItem

/**
 * @author yinhang
 * @since 2021/7/3
 */
class MainTabFragmentAdapter(
    fragmentManager: FragmentManager,
    private val fragmentList: List<FragmentItem>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position].fragment
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): String {
        return fragmentList[position].title
    }

    fun getIcon(position: Int): Int? {
        return fragmentList[position].icon
    }
}