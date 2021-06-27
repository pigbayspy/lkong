package io.pig.lkong.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.account.UserAccount
import io.pig.lkong.account.UserAccountManager
import kotlinx.coroutines.launch

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