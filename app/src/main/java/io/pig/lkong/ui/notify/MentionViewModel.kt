package io.pig.lkong.ui.notify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.TimelineModel
import kotlinx.coroutines.launch

class MentionViewModel : ViewModel() {

    val date = MutableLiveData(System.currentTimeMillis())
    val loading = MutableLiveData(false)
    val mentions = MutableLiveData(emptyList<TimelineModel>())

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
                val respBase = LkongRepository.getAtMe(date.value!!)
                if (respBase.data != null) {
                    val result = respBase.data.atme.data
                    val mentionModels = result.map {
                        TimelineModel(it)
                    }
                    mentions.value = mentions.value.let {
                        if (it == null) {
                            mentionModels
                        } else {
                            it + mentionModels
                        }
                    }
                    date.value = respBase.data.atme.nextDate
                    hasMore = respBase.data.atme.hasMore
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
        this.mentions.value = emptyList()
        getMentions()
    }

    companion object {
        private const val TAG = "MentionViewModel"
    }
}