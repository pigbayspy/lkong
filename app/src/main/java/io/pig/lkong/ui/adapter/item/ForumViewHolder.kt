package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/10
 */
class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val forumIconImage = itemView.findViewById<ImageView>(R.id.recycle_view_item_forum_image_view_icon)
    val forumTitleText = itemView.findViewById<TextView>(R.id.recycle_view_item_forum_text_view_title)
    val forumSecondaryText = itemView.findViewById<TextView>(R.id.recycle_view_item_forum_text_view_secondary)
}