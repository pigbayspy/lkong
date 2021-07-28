package io.pig.lkong.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.pig.lkong.model.BrowseHistoryModel
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    val histories = MutableLiveData<List<BrowseHistoryModel>>()

    fun getHistory() {
        viewModelScope.launch {
            
        }
    }
}