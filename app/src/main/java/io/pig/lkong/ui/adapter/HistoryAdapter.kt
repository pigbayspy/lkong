package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.model.BrowseHistoryModel
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/7/28
 */
class HistoryAdapter(
    val context: Context,
    histories: List<BrowseHistoryModel>
) : BaseRecycleViewAdapter<BrowseHistoryModel>(histories) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}