package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.widget.PostItemView

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val postItem: PostItemView
    val avatarImage: ImageView
    val rateButton: ImageButton
    val rateText: TextView
    val shareButton: ImageButton
    val editButton: ImageButton

    init {
        postItem = itemView.findViewById(R.id.recycle_item_post_view_item)
        avatarImage = itemView.findViewById(R.id.recycle_item_post_image_avatar)
        rateText = itemView.findViewById(R.id.recycle_item_post_text_rate)
        rateButton = itemView.findViewById(R.id.recycle_item_post_button_rate)
        shareButton = itemView.findViewById(R.id.recycle_item_post_button_share)
        editButton = itemView.findViewById(R.id.recycle_item_post_button_edit)
    }
}