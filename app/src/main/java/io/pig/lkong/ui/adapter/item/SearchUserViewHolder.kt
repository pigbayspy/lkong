package io.pig.lkong.ui.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R

class SearchUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val avatarImage: ImageView = itemView.findViewById(R.id.item_search_user_icon)
    val nameText: TextView = itemView.findViewById(R.id.item_search_user_name)
    val signText: TextView = itemView.findViewById(R.id.item_search_user_sign)
}