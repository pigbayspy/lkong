package io.pig.lkong.ui.timeline

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

    fun getTimeline() {
        viewModelScope.launch {
            val respData = LkongRepository.getTimeline(time)
            if (respData.data != null) {
                time = respData.data.feeds.nextTime
                val timelineModels = respData.data.feeds.data.map {
                    TimelineModel(it)
                }
                timelines.value = timelines.value!! + timelineModels
            }
        }
    }

    fun refresh() {
        this.time = System.currentTimeMillis()
        viewModelScope.launch {
            val respData = LkongRepository.getTimeline(time)
            if (respData.data != null) {
                time = respData.data.feeds.nextTime
                val timelineModels = respData.data.feeds.data.map {
                    TimelineModel(it)
                }
                timelines.value = timelineModels
            }
        }
    }
}