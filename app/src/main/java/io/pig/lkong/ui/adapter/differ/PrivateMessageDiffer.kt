package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.PrivateMessageModel

object PrivateMessageDiffer : DiffUtil.ItemCallback<PrivateMessageModel>() {

    override fun areContentsTheSame(
        oldItem: PrivateMessageModel,
        newItem: PrivateMessageModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(
        oldItem: PrivateMessageModel,
        newItem: PrivateMessageModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
}