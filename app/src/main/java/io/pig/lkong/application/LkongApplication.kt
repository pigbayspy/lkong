package io.pig.lkong.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.preference.Prefs

/**
 * @author yinhang
 * @since 2021/5/16
 */
@HiltAndroidApp
class LkongApplication : Application() {

    private lateinit var userAccountMgr: UserAccountManager

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
    }

}