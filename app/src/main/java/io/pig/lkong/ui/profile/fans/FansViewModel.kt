package io.pig.lkong.ui.profile.fans

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.FansModel
import kotlinx.coroutines.launch

class FansViewModel(val userId: Long) : ViewModel() {

    val page = MutableLiveData(1)
    val loading = MutableLiveData(false)
    val fans = MutableLiveData<List<FansModel>>(emptyList())

    private var isLoadMore = false

    fun getFans() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getFans(userId, page.value!!)
                val result = respBase.data?.fansList ?: emptyList()
                val fansModel = result.map {
                    FansModel(it)
                }
                fans.value = fans.value.let {
                    if (it == null) {
                        fansModel
                    } else {
                        it + fansModel
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
        this.fans.value = emptyList()
        getFans()
    }

    companion object {
        private const val TAG = "FansViewModel"
    }
}