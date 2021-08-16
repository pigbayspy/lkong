package io.pig.lkong.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.UserModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/7/17
 */
class UserProfileViewModel : ViewModel() {

    val user = MutableLiveData<UserModel>()

    fun getUserProfile(uid: Long) {
        viewModelScope.launch {
            val respBase = LkongRepository.getUserProfile(uid)
            if (respBase.data != null) {
                val userProfile = respBase.data
                val userModel = UserModel(userProfile)
                user.value = userModel
            }
        }
    }
}