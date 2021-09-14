package io.pig.lkong.ui.notify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.NoticeModel
import kotlinx.coroutines.launch

class NoticeViewModel : ViewModel() {

    val date = MutableLiveData(System.currentTimeMillis())
    val loading = MutableLiveData(false)
    val notices = MutableLiveData(emptyList<NoticeModel>())

    private var hasMore = true
    private var isLoadMore = false

    fun getMentions() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getSystemNotice(date.value!!)
                if (respBase.data != null) {
                    val result = respBase.data.notice.data
                    val mentionModels = result.map {
                        NoticeModel(it)
                    }
                    notices.value = notices.value.let {
                        if (it == null) {
                            mentionModels
                        } else {
                            it + mentionModels
                        }
                    }
                    date.value = respBase.data.notice.nextDate
                    hasMore = respBase.data.notice.hasMore
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
        this.notices.value = emptyList()
        getMentions()
    }

    companion object {
        private const val TAG = "NoticeViewModel"
    }
}