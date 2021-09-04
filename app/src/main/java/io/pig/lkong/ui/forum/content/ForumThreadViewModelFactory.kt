package io.pig.lkong.ui.forum.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForumThreadViewModelFactory(private val forumId: Long, private val forumName: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForumThreadViewModel(forumId, forumName) as T
    }
}