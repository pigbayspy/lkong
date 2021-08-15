package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.listener.ForumThreadModel

/**
 * @author yinhang
 * @since 2021/8/15
 */
object ForumThreadDiffer : DiffUtil.ItemCallback<ForumThreadModel>() {
    override fun areContentsTheSame(oldItem: ForumThreadModel, newItem: ForumThreadModel): Boolean {
        return oldItem.tid == newItem.tid
    }

    override fun areItemsTheSame(oldItem: ForumThreadModel, newItem: ForumThreadModel): Boolean {
        return oldItem.tid == newItem.tid
    }
}