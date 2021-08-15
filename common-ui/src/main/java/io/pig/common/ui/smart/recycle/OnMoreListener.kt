package io.pig.common.ui.smart.recycle

interface OnMoreListener {
    /**
     * @param overallItemsCount      超过的数量
     * @param itemsBeforeMore        之前的数量
     * @param maxLastVisiblePosition for staggered grid this is max of all spans
     */
    fun onMoreAsked(overallItemsCount: Int, itemsBeforeMore: Int, maxLastVisiblePosition: Int)

    fun onChangeMoreVisibility(visibility: Int)
}