package io.pig.lkong.ui.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import io.pig.lkong.model.TimelineModel

/**
 * @author yinhang
 * @since 2021/8/14
 */
object TimelineDiffer : DiffUtil.ItemCallback<TimelineModel>() {

    override fun areItemsTheSame(oldItem: TimelineModel, newItem: TimelineModel): Boolean {
        return oldItem.pid == newItem.pid
    }

    override fun areContentsTheSame(oldItem: TimelineModel, newItem: TimelineModel): Boolean {
        return oldItem.pid == newItem.pid
    }
}