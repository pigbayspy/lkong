package io.pig.lkong.sync

import android.accounts.Account
import android.content.ContentResolver
import android.os.Bundle

/**
 * 同步工具类
 *
 * @author yinhang
 * @since 2021/5/23
 */
object SyncUtil {

    const val SYNC_AUTHORITY_CHECK_NOTICE = "io.pig.lkong.data.provider.checknotice"

    const val SYNC_FREQ_ONE_HOUR = 3600L

    const val SYNC_FREQ_HALF_HOUR = 1800L

    fun manualSync(account: Account, auth: String) {
        val settingsBundle = Bundle()
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
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