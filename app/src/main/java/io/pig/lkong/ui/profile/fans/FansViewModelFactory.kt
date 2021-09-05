package io.pig.lkong.ui.profile.fans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FansViewModelFactory(private val userId: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FansViewModel(userId) as T
    }
}