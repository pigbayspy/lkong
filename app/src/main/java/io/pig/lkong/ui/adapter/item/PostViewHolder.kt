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

    val postItem: PostItemView = itemView.findViewById(R.id.item_post_view_item)
    val avatarImage: ImageView = itemView.findViewById(R.id.item_post_image_avatar)
    val rateButton: ImageButton = itemView.findViewById(R.id.item_post_button_rate)
    val rateText: TextView = itemView.findViewById(R.id.item_post_text_rate)
    val shareButton: ImageButton = itemView.findViewById(R.id.item_post_button_share)
    val editButton: ImageButton = itemView.findViewById(R.id.item_post_button_edit)
}