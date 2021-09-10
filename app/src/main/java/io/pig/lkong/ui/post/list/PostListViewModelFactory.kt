package io.pig.lkong.ui.post.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostListViewModelFactory(private val thread: Long) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostListViewModel(thread) as T
    }
}