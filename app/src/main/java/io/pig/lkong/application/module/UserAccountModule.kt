package io.pig.lkong.application.module

import android.content.Context
import dagger.Module
import dagger.Provides
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.LkongApplication
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/05/19
 */
@Module(includes = [LkongModule::class])
class UserAccountModule {
    @Singleton
    @Provides
    fun provideUserAccountManager(context: Context): UserAccountManager {
        val application = context as LkongApplication
        return application.getUserAccountManager()
    }
}