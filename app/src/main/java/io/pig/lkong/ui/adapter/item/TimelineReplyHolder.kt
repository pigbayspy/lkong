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

    val secondaryContainer: RelativeLayout
    val secondaryMessageText: TextView
    val thirdMessageText: TextView

    init {
        itemView.apply {
            secondaryContainer = findViewById(R.id.secondary_message_container)
            secondaryMessageText = findViewById(R.id.recycle_item_timeline_secondary_message)
            thirdMessageText = findViewById(R.id.recycle_item_timeline_third_message)
        }
    }
}