package io.pig.lkong.application.module

import android.accounts.AccountManager
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/5/16
 */
@Module
class LkongModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideAccountManager(): AccountManager {
        return AccountManager.get(context)
    }
}