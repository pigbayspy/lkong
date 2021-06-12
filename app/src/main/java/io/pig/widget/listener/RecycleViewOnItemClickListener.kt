package io.pig.widget.listener

import android.view.View

/**
 * @author yinhang
 * @since 2021/6/13
 */
interface RecycleViewOnItemClickListener {
    fun onItemClick(view: View, pos: Int, id: Long)
}