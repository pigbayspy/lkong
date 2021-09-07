package io.pig.lkong.ui.profile.followers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.FansModel
import kotlinx.coroutines.launch

class FollowersViewModel(val userId: Long) : ViewModel() {

    val page = MutableLiveData(1)
    val loading = MutableLiveData(false)
    val followers = MutableLiveData<List<FansModel>>(emptyList())

    private var isLoadMore = false

    fun getFollowers() {
        if (isLoadMore) {
            return
        }
        isLoadMore = true
        loading.value = true
        viewModelScope.launch {
            try {
                val respBase = LkongRepository.getFollowers(userId, page.value!!)
                val result = respBase.data?.followList ?: emptyList()
                val fansModel = result.map {
                    FansModel(it)
                }
                followers.value = followers.value.let {
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
        this.followers.value = emptyList()
        getFollowers()
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }
}