package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/13
 */
class HotThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val iconImageView: ImageView = itemView.findViewById(R.id.item_hot_thread_image_rank)
    val titleTextView: TextView = itemView.findViewById(R.id.item_hot_thread_text_subject)
}