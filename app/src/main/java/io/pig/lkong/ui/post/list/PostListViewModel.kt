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
class PostListViewModel(private val thread: Long) : ViewModel() {

    companion object {
        private const val TAG = "PostListViewModel"
    }

    class ThreadDetail(val posts: List<PostModel>, val thread: PostRespThreadData)

    val detail = MutableLiveData<ThreadDetail>()
    val page = MutableLiveData(1)

    fun initPage(p: Int) {
        this.page.value = p
    }

    fun getPost() {
        viewModelScope.launch {
            try {
                val result = LkongRepository.getThreadPost(thread, getPage())
                val postModelList = result.data?.posts?.map {
                    PostModel(it)
                } ?: emptyList()
                val t = result.data!!.thread
                detail.value = ThreadDetail(postModelList, t)
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

    fun getPages(): Int {
        val replies = detail.value?.thread?.replies ?: 1
        return if (replies == 0) {
            1
        } else {
            (replies + PostListActivity.PAGE_SIZE - 1) / PostListActivity.PAGE_SIZE
        }
    }

    fun getPage(): Int {
        return page.value ?: 1
    }

    fun goToPage(p: Int) {
        if (p == page.value || p < 1 || p > getPages()) {
            return
        }
        this.page.value = p
    }

    fun goToNextPage() {
        if (getPage() + 1 >= 1 && getPage() + 1 <= getPages()) {
            this.page.value = this.getPage() + 1
        }
    }

    fun goToPrevPage() {
        if (getPage() - 1 >= 1 && getPage() - 1 <= getPages()) {
            this.page.value = this.getPage() - 1
        }
    }
}