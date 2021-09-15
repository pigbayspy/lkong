package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.util.ThemeUtil

/**
 * @author yinhang
 * @since 2021/7/22
 */
open class TimelineViewHolder(
    itemView: View,
    themeKey: String
) : RecyclerView.ViewHolder(itemView) {

    val rootCard: CardView = itemView.findViewById(R.id.item_timeline_card_root_container)
    val authorText: TextView = itemView.findViewById(R.id.item_timeline_text_author_name)
    val datelineText: TextView = itemView.findViewById(R.id.item_timeline_text_dateline)
    val messageText: TextView = itemView.findViewById(R.id.item_timeline_text_message)
    val authorAvatar: ImageView = itemView.findViewById(R.id.item_timeline_image_author_avatar)

    init {
        rootCard.setCardBackgroundColor(
            ThemeUtil.textColorPrimaryInverse(itemView.context, themeKey)
        )
    }
}