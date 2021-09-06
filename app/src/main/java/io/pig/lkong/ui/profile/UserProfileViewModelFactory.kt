package io.pig.lkong.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserProfileViewModelFactory (private val userId: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(userId) as T
    }
}