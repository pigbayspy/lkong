package io.pig.lkong.ui.post.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.PostModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/6/27
 */
class PostListViewModel : ViewModel() {

    companion object {
        private const val TAG = "PostListViewModel"
    }

    val postList = MutableLiveData<List<PostModel>>()

    fun getPost(thread: Long, page: Int) {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getPostList(thread, page)
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
                postList.value = null
            }
        }
    }
}