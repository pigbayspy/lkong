package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.ui.adapter.item.TimelineViewHolder
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineAdapter(
    val context: Context,
    timelines: List<TimelineModel>
) : BaseRecycleViewAdapter<TimelineModel>(timelines) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val isReply = (viewType == TYPE_REPLY)
        val view = if (isReply) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline_thread, parent, false)
        }
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as TimelineViewHolder
        val item = getItem(position)
        val isReply = (getItemViewType(position) == TYPE_REPLY)
        if (isReply) {
            bindReplyItem(viewHolder, item)
        } else {
            bindThreadItem(viewHolder, item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isQuote || !item.isThread) {
            TYPE_REPLY
        } else if (item.isThread) {
            TYPE_THREAD
        } else {
            TYPE_THREAD
        }
    }

    private fun bindThreadItem(holder: TimelineViewHolder, item: TimelineModel) {

    }

    private fun bindReplyItem(holder: TimelineViewHolder, item: TimelineModel) {

    }

    companion object {
        const val TYPE_REPLY = 1
        const val TYPE_THREAD = 0
    }
}