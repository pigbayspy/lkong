package io.pig.lkong.application.modules

import android.accounts.AccountManager
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import io.pig.lkong.application.qualifier.ApplicationContext
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/5/16
 */
@Module
class LkongModule(private val context: Context) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideUserAccountManager(): UserAccountManager {
        val application = context as LkongApplication
        return application.getUserAccountManager()
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideAccountManager(): AccountManager? {
        return AccountManager.get(context)
    }
}