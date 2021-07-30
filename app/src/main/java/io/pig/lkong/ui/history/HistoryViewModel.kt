package io.pig.lkong.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.model.HistoryModel
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    val histories = MutableLiveData<List<HistoryModel>>()

    fun getHistory() {
        viewModelScope.launch {
            
        }
    }
}