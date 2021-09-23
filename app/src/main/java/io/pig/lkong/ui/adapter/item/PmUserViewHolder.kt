package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

class PmUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val avatarImage:ImageView = itemView.findViewById(R.id.item_pm_user_avatar_image)
    val chatMsgText :TextView = itemView.findViewById(R.id.item_pm_user_text_message)
    val authorNameText:TextView = itemView.findViewById(R.id.item_pm_user_text_author)
    val datelineText:TextView = itemView.findViewById(R.id.item_pm_user_text_dateline)
}