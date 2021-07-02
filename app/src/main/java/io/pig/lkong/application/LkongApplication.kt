package io.pig.lkong.application

import android.app.Application
import android.content.Context
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.component.DaggerLkongPresentComponent
import io.pig.lkong.application.component.DaggerUserAccountComponent
import io.pig.lkong.application.component.LkongPresentComponent
import io.pig.lkong.application.component.UserAccountComponent
import io.pig.lkong.application.module.LkongModule
import io.pig.lkong.application.module.UserAccountModule
import io.pig.lkong.http.provider.LkongServiceProvider
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

    private lateinit var userAccountComponent: UserAccountComponent

    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
        userAccountMgr = UserAccountManager()
        initComponent()
        userAccountComponent.inject(userAccountMgr)
        userAccountMgr.init()
        LkongServiceProvider.addAccount(userAccountMgr.getAuthObject())
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
            .userAccountModule(UserAccountModule())
            .build()
        userAccountComponent = DaggerUserAccountComponent
            .builder()
            .lkongModule(LkongModule(this))
            .build()
    }
}