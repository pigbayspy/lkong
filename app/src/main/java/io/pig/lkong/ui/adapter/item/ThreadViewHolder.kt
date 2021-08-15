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

    val avatarImage: ImageView
    val threadTitleText: TextView
    val threadAuthorNameText: TextView
    val threadRepliesText: TextView
    val threadDatetimeText: TextView

    init {
        itemView.apply {
            threadTitleText = findViewById(R.id.item_thread_text_title)
            avatarImage = findViewById(R.id.item_thread_image_icon)
            threadAuthorNameText = findViewById(R.id.item_thread_text_username)
            threadRepliesText = findViewById(R.id.item_thread_text_reply_count)
            threadDatetimeText = findViewById(R.id.item_thread_text_datetime)
            setOnClickListener {
                listener.onItemThreadClick(it, adapterPosition)
            }
        }
        this.avatarImage.setOnClickListener {
            listener.onProfileAreaClick(it, adapterPosition, 0)
        }
    }
}