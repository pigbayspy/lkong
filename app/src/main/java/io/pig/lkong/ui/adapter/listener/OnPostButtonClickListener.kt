package io.pig.lkong.ui.adapter.listener

import android.view.View
import io.pig.lkong.model.PostModel

/**
 * @author yinhang
 * @since 2021/6/28
 */
interface OnPostButtonClickListener {

    fun onPostTextLongClick(view: View, post: PostModel)

    fun onRateClick(view: View, pid: String, tid: Long)

    fun onRateTextClick(view: View, rates: List<PostModel.PostRateModel>)

    fun onShareClick(view: View, position: Int)

    fun onReplyClick(view: View, position: Int)

    fun onEditClick(view: View, position: Int)

    fun onProfileImageClick(view: View, uid: Long)
}