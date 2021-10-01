package io.pig.lkong.ui.profile.reply

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.TimelineModel
import kotlinx.coroutines.launch

class ReplyViewModel(val userId: Long) : ViewModel() {

    val page = MutableLiveData(1)
    val loading = MutableLiveData(false)
    val replies = MutableLiveData<List<TimelineModel>>(emptyList())

    private var isLoadMore = false

    fun getReplies() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getUserReply(userId, page.value!!)
                val result = respBase.data?.userReplies ?: emptyList()
                val timelineModels = result.filterNotNull().map {
                    TimelineModel(it)
                }.filter {
                    if (it.quoteInfo != null) {
                        return@filter true
                    } else if (it.threadInfo != null) {
                        return@filter false
                    }
                    return@filter true
                }
                replies.value = replies.value.let {
                    if (it == null) {
                        timelineModels
                    } else {
                        it + timelineModels
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            isLoadMore = false
            loading.value = false
        }
    }

    fun clear() {
        this.replies.value = emptyList()
        getReplies()
    }

    companion object {
        private const val TAG = "ReplyViewModel"
    }
}