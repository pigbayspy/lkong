package io.pig.lkong

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.databinding.ActivityMainBinding
import io.pig.lkong.exception.SignInException
import io.pig.lkong.http.provider.LkongServiceProvider
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.BoolPrefs
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION_VALUE
import io.pig.lkong.preference.PrefConst.FORUMS_FIRST
import io.pig.lkong.preference.PrefConst.FORUMS_FIRST_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.AccountChangeEvent
import io.pig.lkong.sync.SyncUtil
import io.pig.lkong.ui.adapter.MainTabFragmentAdapter
import io.pig.lkong.ui.adapter.item.FragmentItem
import io.pig.lkong.ui.forum.ForumsFragment
import io.pig.lkong.ui.forum.follow.FollowForumsFragment
import io.pig.lkong.ui.main.MainViewModel
import io.pig.lkong.ui.thread.digest.DigestThreadFragment
import io.pig.lkong.ui.thread.hot.HotThreadFragment
import io.pig.lkong.ui.timeline.TimeLineFragment
import io.pig.lkong.util.ImageLoaderUtil
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        val Running = AtomicBoolean(false)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var pages: ViewPager
    private lateinit var tabs: TabLayout

    private lateinit var checkNoticeDuration: StringPrefs
    private lateinit var forumsFirst: BoolPrefs

    @Inject
    lateinit var userAccountMgr: UserAccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Running.set(true)
        injectThis()

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 检查是否登录
        if (!userAccountMgr.isSignedIn()) {
            AppNavigation.navigateToSignInActivity(this)
            finish()
            return
        }
        // 设置用户cookie
        LkongServiceProvider.addAccount(userAccountMgr.getAuthObject())

        // 初始化配置
        initConfig()

        setSupportActionBar(binding.appBarMain.mainToolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        val topLevel = setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow,
            R.id.nav_setting
        )
        appBarConfiguration = AppBarConfiguration(topLevel, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Todo
        return when (item.itemId) {
            R.id.action_main_evening -> false
            R.id.action_main_logout -> false
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Running.set(false)
    }

    override fun onPostResume() {
        super.onPostResume()
        val account = userAccountMgr.getCurrentUserAccount().account
        SyncUtil.manualSync(account, SyncUtil.SYNC_AUTHORITY_CHECK_NOTICE)
        SyncUtil.setPeriodicSync(
            account, SyncUtil.SYNC_AUTHORITY_CHECK_NOTICE, false,
            checkNoticeDuration.get().toLong()
        )
    }

    private fun injectThis() {
        LkongApplication.get(this).presentComponent().inject(this)
    }

    private fun initDrawer() {
        // 设置 ViewModel
        val headView = binding.navView.getHeaderView(0)
        val userEmailView = headView.findViewById<TextView>(R.id.account_email)
        val userNameView = headView.findViewById<TextView>(R.id.account_name)
        val userAvatarView = headView.findViewById<ImageView>(R.id.account_avatar)
        mainViewModel.currentAccount.observe(this) {
            userEmailView.text = it.userEmail
            userNameView.text = it.userName
            ImageLoaderUtil.loadAvatar(this, userAvatarView, it.userAvatar)
        }
        addAccountProfile()

        // 初始化 tab
        pages = binding.appBarMain.fragmentContentMainPager
        tabs = binding.appBarMain.fragmentContentMainTab
        tabs.setupWithViewPager(pages)
        setupViewPager()
    }

    private fun addAccountProfile() {
        try {
            mainViewModel.getAccounts(userAccountMgr)
        } catch (e: SignInException) {
            AppNavigation.navigateToSignInActivity(this)
            finish()
            return
        }
        RxEventBus.sendEvent(AccountChangeEvent())
    }

    private fun setupViewPager() {
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

        val fragmentAdapter = MainTabFragmentAdapter(supportFragmentManager, fragments)
        pages.adapter = fragmentAdapter
        for (i in 0..tabs.tabCount) {
            val tab = tabs.getTabAt(i)
            tab?.apply {
                setIcon(fragmentAdapter.getIcon(i))
                text = ""
            }
        }
    }

    private fun initConfig() {
        // 初始化检查通知时间
        checkNoticeDuration = Prefs.getStringPrefs(
            CHECK_NOTIFICATION_DURATION,
            CHECK_NOTIFICATION_DURATION_VALUE
        )
        // 初始化界面设置
        forumsFirst = Prefs.getBoolPrefs(
            FORUMS_FIRST,
            FORUMS_FIRST_VALUE
        )
    }
}