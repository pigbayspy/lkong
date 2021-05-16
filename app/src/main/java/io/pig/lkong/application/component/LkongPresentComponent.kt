package io.pig.lkong.application.component

import dagger.Component
import io.pig.lkong.MainActivity
import io.pig.lkong.application.modules.LkongModule
import javax.inject.Singleton

/**
 * @author yinhang
 * @since 2021/5/16
 */
@Singleton
@Component(modules = [LkongModule::class])
interface LkongPresentComponent {

    fun inject(activity: MainActivity)
}