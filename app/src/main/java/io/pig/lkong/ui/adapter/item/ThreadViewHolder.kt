package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener

/**
 * @author yinhang
 * @since 2021/6/13
 */
class ThreadViewHolder(
    itemView: View,
    private val listener: OnThreadClickListener
) : RecyclerView.ViewHolder(itemView) {

    val avatarView: ImageView
    val threadTitleView: TextView
    val threadUserNameView: TextView
    val threadReplyCountView: TextView
    val threadDatetimeView: TextView

    init {
        this.threadTitleView = itemView.findViewById(R.id.item_thread_text_title)
        this.avatarView = itemView.findViewById(R.id.item_thread_image_icon)
        this.threadUserNameView = itemView.findViewById(R.id.item_thread_text_username)
        this.threadReplyCountView = itemView.findViewById(R.id.item_thread_text_reply_count)
        this.threadDatetimeView = itemView.findViewById(R.id.item_thread_text_datetime)
        avatarView.setOnClickListener {
            listener.onProfileAreaClick(it, adapterPosition, 0)
        }
        itemView.setOnClickListener {
            listener.onItemThreadClick(it, adapterPosition)
        }
    }
}