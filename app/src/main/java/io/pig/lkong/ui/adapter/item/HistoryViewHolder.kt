package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/29
 */
class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleText: TextView = itemView.findViewById(R.id.item_browse_history_text_title)
    val secondaryText: TextView = itemView.findViewById(R.id.item_browse_history_text_secondary)
    val timeText: TextView = itemView.findViewById(R.id.item_browse_history_text_time)
}