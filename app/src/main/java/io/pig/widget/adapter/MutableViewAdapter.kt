package io.pig.widget.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * @author yinhang
 * @since 2021/8/14
 */
abstract class MutableViewAdapter<T>(differ: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, RecyclerView.ViewHolder>(differ) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val onClickView = holder.itemView
        onClickView.tag = position
    }
}