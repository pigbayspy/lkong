package io.pig.lkong.ui.profile.thread

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.UserThreadModel
import kotlinx.coroutines.launch

class UserThreadsViewModel(val userId: Long) : ViewModel() {

    val page = MutableLiveData(1)
    val loading = MutableLiveData(false)
    val threads = MutableLiveData<List<UserThreadModel>>(emptyList())

    private var isLoadMore = false

    fun goToNextPage() {
        if (isLoadMore) {
            return
        }
        this.page.value = 1 + (this.page.value ?: 0)
    }

    fun getThreads() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getUserThreads(userId, page.value!!)
                val result = respBase.data?.userThreads ?: emptyList()
                val threadModels = result.map {
                    UserThreadModel(it)
                }
                threads.value = threads.value.let {
                    if (it == null) {
                        threadModels
                    } else {
                        it + threadModels
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
        this.threads.value = emptyList()
        getThreads()
    }

    companion object {
        private const val TAG = "UserThreadsViewModel"
    }
}