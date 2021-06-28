package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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

    init {
        val avatarImage = itemView.findViewById<ImageView>(R.id.recycle_item_post_image_avatar)
        val rateButton = itemView.findViewById<ImageButton>(R.id.recycle_item_post_button_rate)
        val shareButton = itemView.findViewById<ImageButton>(R.id.recycle_item_post_button_share)
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