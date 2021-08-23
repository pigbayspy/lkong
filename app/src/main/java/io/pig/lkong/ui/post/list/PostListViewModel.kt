package io.pig.lkong.ui.post.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.data.LkongDatabase
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
                val result = LkongRepository.getThreadPost(thread, page)
                val postModelList = result.data?.posts?.map {
                    PostModel(it)
                } ?: emptyList()
                postList.value = postModelList
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
        }
    }

    fun saveHistory(lkongDataBase: LkongDatabase, userId: Long) {
        viewModelScope.launch {
            lkongDataBase.saveBrowseHistory(
                userId = userId,
                threadId = 0,
                threadTitle = "",
                forumId = -1,
                forumTitle = "",
                postId = 0,
                authorId = 0,
                authorName = ""
            )
        }
    }
}