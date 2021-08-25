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

    class ThreadDetail(val posts: List<PostModel>, val thread: PostRespThreadData)

    val detail = MutableLiveData<ThreadDetail>()
    val page = MutableLiveData(1)

    fun setPage(p: Int) {
        this.page.value = p
    }

    fun getPost(thread: Long, page: Int) {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getThreadPost(thread, page)
                val postModelList = result.data?.posts?.map {
                    PostModel(it)
                } ?: emptyList()
                detail.value = ThreadDetail(postModelList, result.data!!.thread)
            } catch (e: Exception) {
                Log.e(TAG, "Lkong Network Request Fail", e)
            }
        }
    }

    fun saveHistory(lkongDataBase: LkongDatabase, userId: Long, postPos: Int) {
        detail.value?.let {
            viewModelScope.launch {
                lkongDataBase.saveBrowseHistory(
                    userId = userId,
                    threadId = it.thread.tid,
                    threadTitle = it.thread.title,
                    forumId = it.thread.forum.fid,
                    forumTitle = it.thread.forum.name,
                    postId = it.posts[postPos].pid,
                    authorId = it.thread.author.uid,
                    authorName = it.thread.author.name
                )
            }
        }
    }
}