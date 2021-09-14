package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.util.ThemeUtil

class NoticeViewHolder(itemView: View, themeKey: String) : RecyclerView.ViewHolder(itemView) {

    val card: CardView = itemView.findViewById(R.id.item_notice_card)
    val text: TextView = itemView.findViewById(R.id.item_notice_text_message)

    init {
        card.setBackgroundColor(ThemeUtil.textColorPrimaryInverse(itemView.context, themeKey))
    }
}