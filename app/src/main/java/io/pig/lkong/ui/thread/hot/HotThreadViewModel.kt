package io.pig.lkong.ui.thread.hot

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.HotThreadModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/7/13
 */
class HotThreadViewModel : ViewModel() {

    val hotThreads = MutableLiveData<List<HotThreadModel>>(emptyList())
    val loading = MutableLiveData(false)

    fun getHotThreads() {
        loading.value = true
        hotThreads.value = emptyList()
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getHot()
                val hotThreadData = respBase.data?.hots ?: emptyList()
                val threads = hotThreadData.map {
                    HotThreadModel(it.tid, it.title)
                }
                hotThreads.value = threads
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
            loading.value = false
        }
    }

    fun refresh() {
        getHotThreads()
    }

    companion object {
        private const val TAG = "HotThreadViewModel"
    }

}
