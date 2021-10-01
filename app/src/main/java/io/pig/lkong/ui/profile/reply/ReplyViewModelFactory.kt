package io.pig.lkong.ui.profile.reply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReplyViewModelFactory(private val userId: Long) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReplyViewModel(userId) as T
    }
}