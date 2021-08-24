package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.HistoryModel
import io.pig.lkong.model.listener.OnItemThreadClickListener
import io.pig.lkong.ui.adapter.item.HistoryViewHolder
import io.pig.lkong.util.DateUtil
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/7/28
 */
class HistoryAdapter(
    private val context: Context,
    private val clickListener: OnItemThreadClickListener,
    histories: List<HistoryModel>
) : FixedViewAdapter<HistoryModel>(histories) {

    private val todayPrefix = context.getString(R.string.text_datetime_today)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_browse_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as HistoryViewHolder
        val item = getItem(position)
        viewHolder.titleText.text = item.threadTitle
        viewHolder.secondaryText.text = getDescribe(item.forumTitle, item.authorName)
        viewHolder.timeText.text = DateUtil.formatDateByToday(item.lastReadTime, todayPrefix)
        viewHolder.itemView.setOnClickListener {
            clickListener.onItemThreadClick(it, item.threadId)
        }
    }

    private fun getDescribe(forum: String, author: String): String {
        return "$forum - $author"
    }
}