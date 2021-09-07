package io.pig.lkong.ui.profile.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FollowersViewModelFactory(private val userId: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowersViewModel(userId) as T
    }
}