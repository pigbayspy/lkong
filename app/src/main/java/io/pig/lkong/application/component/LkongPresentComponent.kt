package io.pig.lkong.application.component

import dagger.Component
import io.pig.lkong.MainActivity
import io.pig.lkong.application.module.LkongModule
import io.pig.lkong.application.module.UserAccountModule
import io.pig.lkong.ui.post.list.PostListActivity
import io.pig.lkong.ui.setting.SettingFragment
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/5/16
 */
@Singleton
@Component(modules = [LkongModule::class, UserAccountModule::class])
interface LkongPresentComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: PostListActivity)

    fun inject(fragment: SettingFragment)
}