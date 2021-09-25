package io.pig.lkong.ui.adapter.item

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

class PrivateMsgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val avatarImage: ImageView = itemView.findViewById(R.id.item_private_message_image_avatar)
    val messageText: TextView = itemView.findViewById(R.id.item_private_message_text_message)
    val datelineText: TextView = itemView.findViewById(R.id.item_private_message_text_dateline)

    init {
        messageText.movementMethod = LinkMovementMethod.getInstance()
    }
}