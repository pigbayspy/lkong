package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostViewHolder(
    itemView: View,
    private val listener: OnPostButtonClickListener
) : RecyclerView.ViewHolder(itemView) {

    val avatarImage: ImageView
    val rateButton: ImageButton
    val rateText: TextView
    val shareButton: ImageButton
    val editButton: ImageButton

    init {
        avatarImage = itemView.findViewById(R.id.recycle_item_post_image_avatar)
        rateText = itemView.findViewById(R.id.recycle_item_post_text_rate)
        rateButton = itemView.findViewById(R.id.recycle_item_post_button_rate)
        shareButton = itemView.findViewById(R.id.recycle_item_post_button_share)
        editButton = itemView.findViewById(R.id.recycle_item_post_button_edit)
        avatarImage.setOnClickListener {
            listener.onProfileImageClick(it, adapterPosition)
        }
        rateButton.setOnClickListener {
            listener.onRateClick(it, adapterPosition)
        }
        shareButton.setOnClickListener {
            listener.onShareClick(it, adapterPosition)
        }
    }
}