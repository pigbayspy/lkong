package io.pig.lkong.ui.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.FavoriteThreadModel
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    val threadList = MutableLiveData<List<FavoriteThreadModel>>()

    fun getThreads(uid: Long) {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getFavorites(uid)
                val threads = result.data?.userFavoriteList ?: emptyList()
                val threadModels = threads.map {
                    FavoriteThreadModel(it)
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