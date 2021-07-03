package io.pig.lkong.ui.gallery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.ThreadModel
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    val threadList = MutableLiveData<List<ThreadModel>>()

    fun getThreads() {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getFavoriteThread()
                val threads = result.data ?: emptyList()
                val threadModels = threads.map {
                    ThreadModel(it)
                }
                threadList.value = threadModels
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
                threadList.value = emptyList()
            }
        }
    }

    companion object {
        private const val TAG = "GalleryViewModel"
    }
}