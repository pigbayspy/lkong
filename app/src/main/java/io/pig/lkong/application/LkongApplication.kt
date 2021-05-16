package io.pig.lkong.application

import android.app.Application
import android.content.Context
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.component.DaggerLkongPresentComponent
import io.pig.lkong.application.component.LkongPresentComponent
import io.pig.lkong.application.modules.LkongModule
import io.pig.lkong.preference.Prefs
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/5/16
 */
@Singleton
class LkongApplication : Application() {

    companion object {
        fun get(context: Context): LkongApplication {
            return context.applicationContext as LkongApplication
        }
    }

    private lateinit var userAccountMgr: UserAccountManager

    private lateinit var presentComponent: LkongPresentComponent

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        userAccountMgr = UserAccountManager(this)
        userAccountMgr.init()
        initComponent()
    }


    fun getUserAccountManager(): UserAccountManager {
        return userAccountMgr
    }

    fun presentComponent(): LkongPresentComponent {
        return this.presentComponent
    }

    private fun initComponent() {
        presentComponent = DaggerLkongPresentComponent
            .builder()
            .lkongModule(LkongModule(this))
            .build()
    }
}