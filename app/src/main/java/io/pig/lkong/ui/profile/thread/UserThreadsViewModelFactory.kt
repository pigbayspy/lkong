package io.pig.lkong.ui.profile.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserThreadsViewModelFactory(private val userId: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserThreadsViewModel(userId) as T
    }
}