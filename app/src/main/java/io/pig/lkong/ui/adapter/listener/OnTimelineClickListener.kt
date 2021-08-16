package io.pig.lkong.ui.adapter.listener

import android.view.View
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.model.listener.OnItemProfileAreaClickListener

/**
 * @author yinhang
 * @since 2021/8/16
 */
interface OnTimelineClickListener :OnItemProfileAreaClickListener {
    fun onItemTimelineClick(view: View, timeline: TimelineModel)
}