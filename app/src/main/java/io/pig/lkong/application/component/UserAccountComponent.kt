package io.pig.lkong.application.component

import dagger.Component
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.module.LkongModule
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/05/19
 */
@Singleton
@Component(modules = [LkongModule::class])
interface UserAccountComponent {

    fun inject(userAccountManager: UserAccountManager)
}