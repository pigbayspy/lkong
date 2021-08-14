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

    val timelines = MutableLiveData<List<TimelineModel>>(emptyList())
    val loading = MutableLiveData(false)

    fun getTimeline() {
        loading.value = true
        viewModelScope.launch {
            try {
                val respData = LkongRepository.getTimeline(time)
                if (respData.data != null) {
                    time = respData.data.feeds.nextTime
                    val timelineModels = respData.data.feeds.data.map {
                        TimelineModel(it)
                    }
                    timelines.value = timelines.value!! + timelineModels
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            loading.value = false
        }
    }

    fun refresh() {
        timelines.value = emptyList()
        loading.value = true
        this.time = System.currentTimeMillis()
        viewModelScope.launch {
            try {
                val respData = LkongRepository.getTimeline(time)
                if (respData.data != null) {
                    time = respData.data.feeds.nextTime
                    val timelineModels = respData.data.feeds.data.map {
                        TimelineModel(it)
                    }
                    timelines.value = timelineModels
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            loading.value = false
        }
    }

    companion object {
        private const val TAG = "TimelineViewModel"
    }
}