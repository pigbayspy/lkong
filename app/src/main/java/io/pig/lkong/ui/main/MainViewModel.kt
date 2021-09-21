package io.pig.lkong.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.pig.lkong.account.UserAccount
import io.pig.lkong.account.UserAccountManager
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.http.data.resp.data.NoticeRespData

/**
 * @author yinhang
 * @since 2021/5/15
 */
class MainViewModel : ViewModel() {

    val currentAccount = MutableLiveData<UserAccount>()
    val userAccounts = MutableLiveData<List<UserAccount>>()
    val notice = MutableLiveData<NoticeRespData>()

    fun getAccounts(accountManager: UserAccountManager) {
        currentAccount.value = accountManager.getCurrentUserAccount()
        userAccounts.value = accountManager.getUserAccounts()
    }

    fun checkNoticeCount(db: LkongDatabase) {
        try {
            val result = db.loadNotice(currentAccount.value!!.userId)
            if (result != null) {
                notice.value = result
            }
        } catch (ex: Exception) {
            Log.e(TAG, "checkNoticeCount fail: cause: ${ex.message}")
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}