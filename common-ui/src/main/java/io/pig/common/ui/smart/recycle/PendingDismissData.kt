package io.pig.common.ui.smart.recycle

import android.view.View

/**
 *
 * The callback interface used by {@link SwipeDismissRecyclerViewTouchListener} to inform its client
 * about a successful dismissal of one or more list item positions.
 *
 * @author yinhang
 * @since 2021/8/15
 */
class PendingDismissData(val position: Int, val view: View) :
    Comparable<PendingDismissData> {

    override fun compareTo(other: PendingDismissData): Int {
        // Sort by descending position
        return other.position - position
    }
}