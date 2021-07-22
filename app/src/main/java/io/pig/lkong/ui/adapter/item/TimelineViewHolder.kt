package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val rootCard: CardView
    val authorText: TextView
    val datelineText: TextView
    val messageText: TextView
    val authorAvatar: ImageView

    init {
        this.rootCard = itemView.findViewById(R.id.recycle_item_timeline_card_root_container)
        this.authorText = itemView.findViewById(R.id.recycle_item_timeline_text_author_name)
        this.datelineText = itemView.findViewById(R.id.recycle_item_timeline_text_dateline)
        this.messageText = itemView.findViewById(R.id.recycle_item_timeline_text_message)
        this.authorAvatar = itemView.findViewById(R.id.recycle_item_timeline_image_author_avatar)
    }
}