package io.pig.lkong.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.pig.lkong.model.Account

/**
 * @author yinhang
 * @since 2021/5/15
 */
class MainViewModel : ViewModel() {

    private val account = Account(
        "红烧猪蹄子",
        "pigbayspy@gmail.com",
        ""
    )


    val accountEmail = MutableLiveData<String>().apply {
        value = account.email
    }

    val accountName = MutableLiveData<String>().apply {
        value = account.name
    }
}