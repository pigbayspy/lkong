package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.NoticeModel

object NoticeDiffer : DiffUtil.ItemCallback<NoticeModel>() {

    override fun areContentsTheSame(oldItem: NoticeModel, newItem: NoticeModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: NoticeModel, newItem: NoticeModel): Boolean {
        return oldItem.id == newItem.id
    }
}