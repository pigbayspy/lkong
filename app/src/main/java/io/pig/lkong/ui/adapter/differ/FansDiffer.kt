package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.FansModel

object FansDiffer : DiffUtil.ItemCallback<FansModel>() {

    override fun areContentsTheSame(oldItem: FansModel, newItem: FansModel): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areItemsTheSame(oldItem: FansModel, newItem: FansModel): Boolean {
        return oldItem.userId == newItem.userId
    }
}