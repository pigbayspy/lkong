package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.UserThreadModel

object UserThreadDiffer : DiffUtil.ItemCallback<UserThreadModel>() {

    override fun areContentsTheSame(oldItem: UserThreadModel, newItem: UserThreadModel): Boolean {
        return oldItem.tid == newItem.tid
    }

    override fun areItemsTheSame(oldItem: UserThreadModel, newItem: UserThreadModel): Boolean {
        return oldItem.tid == newItem.tid
    }
}