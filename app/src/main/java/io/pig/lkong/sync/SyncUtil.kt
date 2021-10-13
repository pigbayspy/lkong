package io.pig.lkong.sync

import android.accounts.Account
import android.content.ContentResolver
import android.os.Bundle
import androidx.core.os.bundleOf

/**
 * 同步工具类
 *
 * @author yinhang
 * @since 2021/5/23
 */
object SyncUtil {

    const val SYNC_AUTHORITY_CHECK_NOTICE = "io.pig.lkong.data.provider.notice"

    const val SYNC_FREQ_ONE_HOUR = 3600L

    const val SYNC_FREQ_HALF_HOUR = 1800L

    fun manualSync(account: Account, auth: String) {
        val settingsBundle = bundleOf(
            ContentResolver.SYNC_EXTRAS_MANUAL to true,
            ContentResolver.SYNC_EXTRAS_EXPEDITED to true
        )
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(account, auth, settingsBundle)
    }

    fun setPeriodicSync(account: Account, auth: String, removePrevious: Boolean, interval: Long) {
        if (removePrevious) {
            ContentResolver.removePeriodicSync(account, auth, Bundle.EMPTY)
        } else {
            val periodicSyncs = ContentResolver.getPeriodicSyncs(account, auth)
            for (periodicSync in periodicSyncs) {
                if (periodicSync.period == interval) {
                    return
                }
            }
        }
        ContentResolver.addPeriodicSync(account, auth, Bundle.EMPTY, interval)
    }
}