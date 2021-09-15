package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/25
 */
class TimelineReplyHolder(itemView: View, themeKey: String) :
    TimelineViewHolder(itemView, themeKey) {

    val secondaryContainer: RelativeLayout = itemView.findViewById(R.id.secondary_message_container)
    val secondaryMessageText: TextView = itemView.findViewById(R.id.item_timeline_secondary_message)
    val thirdMessageText: TextView = itemView.findViewById(R.id.item_timeline_third_message)
}