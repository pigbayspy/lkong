package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import io.pig.lkong.R

class PmReceiveMsgViewHolder(itemView: View) : PmSendMsgViewHolder(itemView) {

    val avatarImage: ImageView = itemView.findViewById(R.id.item_private_message_image_avatar)
}