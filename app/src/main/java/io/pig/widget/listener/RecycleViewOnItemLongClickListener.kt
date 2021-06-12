package io.pig.widget.listener

import android.view.View

/**
 * @author yinhang
 * @since 2021/6/13
 */
interface RecycleViewOnItemLongClickListener {

    fun onItemLongClick(view: View, pos: Int, id: Long)
}