package io.pig.lkong.ui.pm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.PrivateMessageModel
import kotlinx.coroutines.launch

class PmViewModel(private val userId: Long, val userAvatar: String?) : ViewModel() {

    val date = MutableLiveData(System.currentTimeMillis())
    val loading = MutableLiveData(false)
    val privateMessages = MutableLiveData(emptyList<PrivateMessageModel>())

    private var hasMore = true
    private var isLoadMore = false

    fun getPrivateMsgs() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getPmMsg(userId, date.value!!)
                if (respBase.data != null) {
                    val result = respBase.data.userMessages.data
                    val mentionModels = result.map {
                        PrivateMessageModel(it)
                    }
                    privateMessages.value = privateMessages.value.let {
                        if (it == null) {
                            mentionModels
                        } else {
                            it + mentionModels
                        }
                    }
                    date.value = respBase.data.userMessages.nextDate
                    hasMore = respBase.data.userMessages.hasMore
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
        this.privateMessages.value = emptyList()
        getPrivateMsgs()
    }

    companion object {
        private const val TAG = "PmViewModel"
    }
}