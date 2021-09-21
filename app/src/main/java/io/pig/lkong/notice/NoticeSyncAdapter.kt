package io.pig.lkong.notice

import android.accounts.Account
import android.accounts.AccountManager
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.application.const.DataContract
import io.pig.lkong.broadcast.BroadcastConst
import io.pig.lkong.data.provider.cacahe.CacheObjectColumns
import io.pig.lkong.data.provider.cacahe.CacheObjectContentValues
import io.pig.lkong.http.data.resp.data.NoticeRespData
import io.pig.lkong.http.source.LkongRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoticeSyncAdapter(context: Context, autoInitialize: Boolean) :
    AbstractThreadedSyncAdapter(context, autoInitialize) {

    companion object {
        private const val TAG = "NoticeSyncAdapter"
        private val gson = Gson()
    }

    private val accountManager = AccountManager.get(context)

    override fun onPerformSync(
        account: Account,
        p1: Bundle?,
        authority: String,
        providerClient: ContentProviderClient,
        p4: SyncResult?
    ) {
        GlobalScope.launch {
            try {
                val respBase = LkongRepository.getNotice()
                if (respBase.data != null) {
                    val notice = respBase.data.data
                    val authObject =
                        UserAccountManager.getUserAccountFromAccountManager(account, accountManager)

                    val json: String = gson.toJson(notice, NoticeRespData::class.java)
                    val values = CacheObjectContentValues()
                    values.putCacheKey(NoticeCacheConst.generateNoticeCountKey(authObject.userId))
                        .putCacheValue(json)

                    providerClient.insert(CacheObjectColumns.contentUri(authority), values.values())
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