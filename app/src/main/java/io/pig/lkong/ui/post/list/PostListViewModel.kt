package io.pig.lkong.ui.post.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.http.data.resp.data.PostRespThreadData
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
    val threadInfo = MutableLiveData<PostRespThreadData>()

    fun getPost(thread: Long, page: Int) {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getThreadPost(thread, page)
                val postModelList = result.data?.posts?.map {
                    PostModel(it)
                } ?: emptyList()
                postList.value = postModelList
                threadInfo.value = result.data?.thread
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
        }
    }

    fun saveHistory(lkongDataBase: LkongDatabase, userId: Long, postPos: Int) {
        val thread = threadInfo.value!!
        val pid = postList.value!![postPos].pid
        viewModelScope.launch {
            lkongDataBase.saveBrowseHistory(
                userId = userId,
                threadId = thread.tid,
                threadTitle = thread.title,
                forumId = thread.forum.fid,
                forumTitle = thread.forum.name,
                postId = pid,
                authorId = thread.author.uid,
                authorName = thread.author.name
            )
        }
    }
}