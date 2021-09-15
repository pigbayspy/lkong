package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/6/13
 */
class ThreadViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    val avatarImage: ImageView = itemView.findViewById(R.id.item_thread_text_title)
    val threadTitleText: TextView = itemView.findViewById(R.id.item_thread_image_icon)
    val threadAuthorNameText: TextView = itemView.findViewById(R.id.item_thread_text_username)
    val threadRepliesText: TextView = itemView.findViewById(R.id.item_thread_text_reply_count)
    val threadDatetimeText: TextView = itemView.findViewById(R.id.item_thread_text_time)
}