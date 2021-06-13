package io.pig.widget.adapter

import androidx.recyclerview.widget.RecyclerView
import io.pig.widget.listener.RecycleViewOnItemClickListener
import io.pig.widget.listener.RecycleViewOnItemLongClickListener

/**
 * @author yinhang
 * @since 2021/6/8
 */
abstract class BaseRecycleViewAdapter<T>(
    private var items: List<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener: RecycleViewOnItemClickListener? = null
    private var longClickListener: RecycleViewOnItemLongClickListener? = null

    fun addAll(newItems: List<T>) {
        this.items = items + newItems
        super.notifyDataSetChanged()
    }

    fun add(newItem: T) {
        this.items = items + newItem
        super.notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
        super.notifyDataSetChanged()
    }

    fun getItem(pos: Int): T {
        return items[pos]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val onClickView = holder.itemView
        onClickView.tag = position
        onClickView.setOnClickListener {
            clickListener?.onItemClick(it, position, getItemId(position))
        }
        onClickView.setOnLongClickListener {
            longClickListener?.onItemLongClick(it, position, getItemId(position))
            return@setOnLongClickListener false
        }
    }

}