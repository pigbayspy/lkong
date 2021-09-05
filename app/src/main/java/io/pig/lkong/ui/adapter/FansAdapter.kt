package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.FansModel
import io.pig.lkong.ui.adapter.differ.FansDiffer
import io.pig.lkong.ui.adapter.item.SearchUserViewHolder
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.widget.adapter.MutableViewAdapter

class FansAdapter(
    private val context: Context
) : MutableViewAdapter<FansModel>(FansDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_user, parent, false)
        return SearchUserViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as SearchUserViewHolder
        val fans = getItem(position)
        viewHolder.nameText.text = fans.name
        ImageLoaderUtil.loadLkongAvatar(context, viewHolder.avatarImage, fans.userId, fans.avatar)
    }
}