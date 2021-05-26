package io.pig.lkong

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.databinding.ActivityMainBinding
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION
import io.pig.lkong.preference.PrefConst.CHECK_NOTIFICATION_DURATION_VALUE
import io.pig.lkong.preference.Prefs
import io.pig.lkong.preference.StringPrefs
import io.pig.lkong.sync.SyncUtil
import io.pig.lkong.ui.main.MainViewModel
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        val Running = AtomicBoolean(false)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var checkNoticeDuration: StringPrefs

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
        }

        // 初始化检查通知时间
        checkNoticeDuration = Prefs.getStringPrefs(
            CHECK_NOTIFICATION_DURATION,
            CHECK_NOTIFICATION_DURATION_VALUE
        )

        setSupportActionBar(binding.appBarMain.mainToolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val topLevel = setOf(
            R.id.nav_home,
            R.id.nav_gallery,
            R.id.nav_slideshow,
            R.id.nav_setting
        )
        appBarConfiguration = AppBarConfiguration(topLevel, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // 设置 ViewModel
        val headView = binding.navView.getHeaderView(0)
        val userEmailView = headView.findViewById<TextView>(R.id.account_email)
        mainViewModel.accountEmail.observe(this) {
            userEmailView.text = it
        }
        val userNameView = headView.findViewById<TextView>(R.id.account_name)
        mainViewModel.accountName.observe(this) {
            userNameView.text = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
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
}