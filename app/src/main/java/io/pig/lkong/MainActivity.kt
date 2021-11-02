package io.pig.lkong

import android.accounts.Account
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.data.impl.LkongDatabaseSqliteImpl
import io.pig.lkong.databinding.ActivityMainBinding
import io.pig.lkong.exception.SignInException
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.rx.RxEventBus
import io.pig.lkong.rx.event.AccountChangeEvent
import io.pig.lkong.sync.SyncUtil
import io.pig.lkong.theme.ThemeConfig
import io.pig.lkong.ui.common.Injectable
import io.pig.lkong.ui.main.MainViewModel
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.TextSizeUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.common.AbstractActivity
import io.pig.ui.snakebar.SnakeBarType
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MainActivity : AbstractActivity(), Injectable {

    companion object {
        val Running = AtomicBoolean(false)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mNotificationMenuItem: MenuItem
    private lateinit var lkongDatabase: LkongDatabase

    private lateinit var checkNoticeDuration: StringPrefs

    private var hasNotice = false

    @Inject
    lateinit var userAccountMgr: UserAccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Running.set(true)
        injectThis()

        // theme
        val lightMode = ThemeConfig(this, ThemeUtil.LIGHT_THEME)
        if (!lightMode.isConfigured(BuildConfig.VERSION_CODE)) {
            lightMode.activityTheme(R.style.AppTheme)
                .primaryColorRes(R.color.colorPrimaryLightDefault)
                .accentColorRes(R.color.colorAccentLightDefault)
                .lightToolbarMode(ThemeUtil.LIGHT_TOOLBAR_AUTO)
                .coloredActionBar(true)
                .coloredNavigationBar(false)
                .textSizeSpForMode(16, TextSizeUtil.TEXT_SIZE_BODY)
                .commit()
        }
        val nightMode = ThemeConfig(this, ThemeUtil.DARK_THEME)
        if (!nightMode.isConfigured(BuildConfig.VERSION_CODE)) {
            nightMode.activityTheme(R.style.AppThemeDark)
                .primaryColorRes(R.color.colorPrimaryDarkDefault)
                .accentColorRes(R.color.colorAccentDarkDefault)
                .lightToolbarMode(ThemeUtil.LIGHT_TOOLBAR_AUTO)
                .coloredActionBar(true)
                .coloredNavigationBar(true)
                .textSizeSpForMode(16, TextSizeUtil.TEXT_SIZE_BODY)
                .commit()
        }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.notice.observe(this) {
            if (it != null) {
                hasNotice = it.hasNotice()
                invalidateOptionsMenu()
            }
        }

        // 检查是否登录
        if (!userAccountMgr.isSignedIn()) {
            AppNavigation.navigateToSignInActivity(this)
            finish()
            return
        }
        // 初始化配置
        initConfig()
        initToolbar()
        initDb()

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)!!
        navController = navHostFragment.findNavController()
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
        mNotificationMenuItem = menu.findItem(R.id.action_main_notify)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (hasNotice) {
            mNotificationMenuItem.setIcon(R.drawable.ic_action_notification_red_dot)
        } else {
            mNotificationMenuItem.setIcon(R.drawable.ic_action_notification)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Todo
        return when (item.itemId) {
            R.id.action_main_evening -> {
                if (isNightMode()) {
                    toggleNightMode()
                }
                true
            }
            R.id.action_main_notify -> {
                AppNavigation.openNotifyActivity(this)
                true
            }
            R.id.action_main_logout -> false
            R.id.action_punch -> {
                lifecycleScope.launch {
                    val respBase = LkongRepository.punch()
                    if (respBase.data == null) {
                        return@launch
                    }
                    val punchResult =
                        getString(R.string.format_punch_day_count, respBase.data.punch.punchday)
                    showSnakeBar(binding.root, punchResult, SnakeBarType.INFO)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Running.set(false)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkNoticeCount(lkongDatabase)
    }

    fun setDrawerTitle(title: String) {
        binding.appBarMain.mainToolbar.title = title
    }

    fun addAppBarView(view: View) {
        binding.appBarMain.mainAppBar.addView(view)
    }

    fun removeAppBarView(view: View) {
        binding.appBarMain.mainAppBar.removeView(view)
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

    override fun injectThis() {
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
            ImageLoaderUtil.loadLkongAvatar(this, userAvatarView, it.userId, it.userAvatar)
            userAvatarView.setOnClickListener { _ ->
                AppNavigation.openUserProfileActivity(this, it.userId)
            }
        }
        addAccountProfile()
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

    private fun initConfig() {
        // 初始化检查通知时间
        checkNoticeDuration = Prefs.getStringPrefs(
            CHECK_NOTIFICATION_DURATION,
            CHECK_NOTIFICATION_DURATION_VALUE
        )
    }

    private fun initDb() {
        this.lkongDatabase = LkongDatabaseSqliteImpl(this)
    }

    private fun initToolbar() {
        val toolbar = binding.appBarMain.mainToolbar
        toolbar.setBackgroundColor(getPrimaryColor())
        setSupportActionBar(toolbar)
        processToolbar(toolbar)
    }

    private fun checkNewNoticeCount() {
        val account: Account = userAccountMgr.getCurrentUserAccount().account
        SyncUtil.manualSync(account, SyncUtil.SYNC_AUTHORITY_CHECK_NOTICE)
        mainViewModel.checkNoticeCount(lkongDatabase)
    }
}