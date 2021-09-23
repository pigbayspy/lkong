package io.pig.lkong.ui.notify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.PmUserModel
import kotlinx.coroutines.launch

class PmViewModel : ViewModel() {

    val date = MutableLiveData(System.currentTimeMillis())
    val loading = MutableLiveData(false)
    val pmUsers = MutableLiveData(emptyList<PmUserModel>())

    private var hasMore = true
    private var isLoadMore = false

    fun getPmUsers() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getPmMsgList(date.value!!)
                if (respBase.data != null) {
                    val result = respBase.data.lastUsersMessages.data
                    val mentionModels = result.map {
                        PmUserModel(it)
                    }
                    pmUsers.value = pmUsers.value.let {
                        if (it == null) {
                            mentionModels
                        } else {
                            it + mentionModels
                        }
                    }
                    date.value = respBase.data.lastUsersMessages.nextDate
                    hasMore = respBase.data.lastUsersMessages.hasMore
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            isLoadMore = false
            loading.value = false
        }
    }

    fun refresh() {
        hasMore = true
        this.date.value = System.currentTimeMillis()
        this.pmUsers.value = emptyList()
        getPmUsers()
    }

    companion object {
        private const val TAG = "PmViewModel"
    }
}