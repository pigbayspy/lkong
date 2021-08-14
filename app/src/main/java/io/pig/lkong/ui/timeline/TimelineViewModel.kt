package io.pig.lkong.ui.timeline

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.TimelineModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/7/25
 */
class TimelineViewModel : ViewModel() {

    private var time = System.currentTimeMillis()
    private var isLoadMore = false

    val timelines = MutableLiveData<List<TimelineModel>>(emptyList())
    val loading = MutableLiveData(false)

    fun getTimeline() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respData = LkongRepository.getTimeline(time)
                if (respData.data != null) {
                    time = respData.data.feeds.nextTime
                    val timelineModels = respData.data.feeds.data.filterNotNull().map {
                        TimelineModel(it)
                    }
                    timelines.value = timelines.value.let {
                        if (it == null) {
                            timelineModels
                        } else {
                            it + timelineModels
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            isLoadMore = false
            loading.value = false
        }
    }

    fun refresh() {
        this.time = System.currentTimeMillis()
        this.timelines.value = null
        getTimeline()
    }

    companion object {
        private const val TAG = "TimelineViewModel"
    }
}