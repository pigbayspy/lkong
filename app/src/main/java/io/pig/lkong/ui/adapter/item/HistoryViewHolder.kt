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

    val titleText: TextView
    val secondaryText: TextView
    val timeText: TextView

    init {
        itemView.apply {
            titleText = findViewById(R.id.recycle_item_browse_history_text_title)
            secondaryText = findViewById(R.id.recycle_item_browse_history_text_secondary)
            timeText = findViewById(R.id.recycle_item_browse_history_text_time)
        }
    }
}