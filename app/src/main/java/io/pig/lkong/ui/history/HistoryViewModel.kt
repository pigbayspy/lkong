package io.pig.lkong.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.data.LkongDatabase
import io.pig.lkong.model.HistoryModel
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    val histories = MutableLiveData<List<HistoryModel>>()

    var start = 0

    fun getHistory(lkongDatabase: LkongDatabase, uid: Long) {
        viewModelScope.launch {
            val result = lkongDatabase.getHistory(uid, start)
            histories.value = result
        }
    }
}