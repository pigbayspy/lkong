package io.pig.lkong.ui.forum.content

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.ui.timeline.TimelineViewModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/8/15
 */
class ForumThreadViewModel : ViewModel() {

    private var page: Int = 0

    val threads = MutableLiveData<List<ForumThreadModel>>(emptyList())

    fun getThreads(fid: Long) {
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getForumThread(fid, page)
                if (respBase.data != null) {
                    val threadModels = respBase.data.threads.map {
                        ForumThreadModel(it)
                    }
                    threads.value = threadModels
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }

        }
    }

    companion object {
        private const val TAG = "ForumThreadViewModel"
    }
}