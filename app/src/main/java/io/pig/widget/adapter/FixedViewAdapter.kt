package io.pig.widget.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * @author yinhang
 * @since 2021/8/14
 */
abstract class FixedViewAdapter<T>(private val items: List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun getItem(pos: Int): T {
        return items[pos]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val onClickView = holder.itemView
        onClickView.tag = position
    }
}