package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

class SearchThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleText: TextView = itemView.findViewById(R.id.item_search_thread_title)
    val secondaryText: TextView = itemView.findViewById(R.id.item_search_thread_secondary)
    val replyCountText: TextView = itemView.findViewById(R.id.item_search_thread_reply_count)
}