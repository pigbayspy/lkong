package io.pig.lkong.ui.pm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PmViewModelFactory(private val userId: Long, private val userAvatar: String?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PmViewModel(userId, userAvatar) as T
    }
}