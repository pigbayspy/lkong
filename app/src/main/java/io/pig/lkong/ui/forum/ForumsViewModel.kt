package io.pig.lkong.ui.forum

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.ForumModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/7/4
 */
class ForumsViewModel : ViewModel() {

    val forums = MutableLiveData<List<ForumModel>>(emptyList())
    val loading = MutableLiveData(true)

    fun refresh() {
        loading.value = true
        forums.value = emptyList()
        getForums()
    }

    fun getForums() {
        viewModelScope.launch {
            try {
                val forumResp = LkongRepository.getForums()
                val forumData = forumResp.data?.forums ?: emptyList()
                val forumList = forumData.map { ForumModel(it) }
                forums.value = forumList
                loading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "Lkong network fail", e)
            }
        }
    }

    companion object {
        private const val TAG = "ForumsViewModel"
    }
}