package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.PmUserModel

object PmUserDiffer : DiffUtil.ItemCallback<PmUserModel>() {

    override fun areContentsTheSame(oldItem: PmUserModel, newItem: PmUserModel): Boolean {
        return oldItem.authorId == newItem.authorId
    }

    override fun areItemsTheSame(oldItem: PmUserModel, newItem: PmUserModel): Boolean {
        return oldItem.authorId == newItem.authorId
    }
}