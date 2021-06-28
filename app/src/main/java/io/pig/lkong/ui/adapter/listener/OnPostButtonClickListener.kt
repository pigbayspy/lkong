package io.pig.lkong.ui.adapter.listener

import android.view.View

/**
 * @author yinhang
 * @since 2021/6/28
 */
interface OnPostButtonClickListener {

    fun onPostTextLongClick(view: View, position: Int)
    fun onRateClick(view: View, position: Int)
    fun onRateTextClick(view: View, position: Int)
    fun onShareClick(view: View, position: Int)
    fun onReplyClick(view: View, position: Int)
    fun onEditClick(view: View, position: Int)
    fun onProfileImageClick(view: View, position: Int)
}