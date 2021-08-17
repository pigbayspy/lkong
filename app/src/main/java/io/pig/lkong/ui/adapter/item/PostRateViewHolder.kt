package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/8/17
 */
class PostRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val userNameText: TextView
    val scoreText: TextView
    val reasonText: TextView

    init {
        itemView.apply {
            userNameText = findViewById(R.id.item_post_rate_text_username)
            scoreText = findViewById(R.id.item_post_rate_text_score)
            reasonText = findViewById(R.id.item_post_rate_text_reason)
        }
    }
}