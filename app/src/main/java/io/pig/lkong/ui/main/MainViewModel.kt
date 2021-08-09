package io.pig.lkong.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.pig.lkong.account.UserAccount
import io.pig.lkong.account.UserAccountManager

/**
 * @author yinhang
 * @since 2021/5/15
 */
class MainViewModel : ViewModel() {

    val currentAccount = MutableLiveData<UserAccount>()
    val userAccounts = MutableLiveData<List<UserAccount>>()

    fun getAccounts(accountManager: UserAccountManager) {
        currentAccount.value = accountManager.getCurrentUserAccount()
        userAccounts.value = accountManager.getUserAccounts()
    }
}