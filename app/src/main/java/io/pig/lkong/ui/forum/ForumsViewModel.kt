package io.pig.lkong.ui.forum

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.application.const.AppConst
import io.pig.lkong.http.source.LkongRepository
import io.pig.lkong.model.ForumModel
import kotlinx.coroutines.launch

/**
 * @author yinhang
 * @since 2021/7/4
 */
class ForumsViewModel : ViewModel() {

    val forums = MutableLiveData<List<ForumModel>>()

    fun getForums() {
        viewModelScope.launch {
            val forumList = ArrayList<ForumModel>(AppConst.MAIN_FORUM_ID_LIST.size)
            for (forum in AppConst.MAIN_FORUM_ID_LIST) {
                try {
                    val forumInfoResp = LkongRepository.getForumInfo(forum)
                    forumList.add(ForumModel(forumInfoResp))
                } catch (e: Exception) {
                    Log.e(TAG, "Lkong Network Request Fail", e)
                }
            }
            forums.value = forumList
        }
    }

    companion object {
        private const val TAG = "ForumsViewModel"
    }
}