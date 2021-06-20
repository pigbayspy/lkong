package io.pig.lkong.ui.gallery

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
            val result = LkongRepository.getFavoriteThread()
            val threads = result.data ?: emptyList()
            val threadModels = threads.map {
                ThreadModel(it)
            }
            threadList.value = threadModels
        }
    }
}