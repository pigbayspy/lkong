package io.pig.lkong.notice

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.broadcast.BroadcastConst
import io.pig.lkong.http.source.LkongRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoticeSyncAdapter(context: Context, autoInitialize: Boolean) :
    AbstractThreadedSyncAdapter(context, autoInitialize) {

    companion object {
        private const val TAG = "NoticeSyncAdapter"
    }

    override fun onPerformSync(
        p0: Account?,
        p1: Bundle?,
        p2: String?,
        p3: ContentProviderClient?,
        p4: SyncResult?
    ) {
        GlobalScope.launch {
            try {
                val respBase = LkongRepository.getNotice()
                if (respBase.data != null) {
                    val notice = respBase.data.data
                    // Todo
                    val broadcastIntent =
                        Intent(BroadcastConst.BROADCAST_SYNC_CHECK_NOTICE_COUNT_DONE)
                    broadcastIntent.putExtra(
                        DataContract.BUNDLE_NOTICE_COUNT_MODEL,
                        notice
                    )
                    context.sendOrderedBroadcast(broadcastIntent, null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong network exception", e)
            }
        }
    }
}