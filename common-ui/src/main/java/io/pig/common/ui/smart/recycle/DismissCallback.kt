package io.pig.common.ui.smart.recycle

import androidx.recyclerview.widget.RecyclerView

/**
 * @author yinhang
 * @since 2021/8/15
 */
interface DismissCallback {
    /**
     * Called to determine whether the given position can be dismissed.
     */
    fun canDismiss(position: Int): Boolean

    /**
     * Called when the user has indicated they she would like to dismiss one or more list item
     * positions.
     *
     * @param recyclerView           The originating [android.widget.ListView].
     * @param reverseSortedPositions An array of positions to dismiss, sorted in descending
     * order for convenience.
     */
    fun onDismiss(recyclerView: RecyclerView, reverseSortedPositions: IntArray)
}