package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PostModel
import io.pig.lkong.ui.adapter.item.PostViewHolder
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostListAdapter(
    val context: Context,
    val userId: Long,
    private val listener: OnPostButtonClickListener,
    postList: List<PostModel>
) : BaseRecycleViewAdapter<PostModel>(postList) {

    private val avatarSize: Int = UiUtil.getDefaultAvatarSize(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as PostViewHolder
        val post = getItem(position)
        // Todo
        if (post.rateScore != 0) {
            viewHolder.rateText.visibility = View.VISIBLE
            viewHolder.rateText.text = post.rateScore.toString()
        }
        if (post.authorId == userId) {
            viewHolder.editButton.visibility = View.VISIBLE
        }
        ImageLoaderUtil.loadAvatar(context, viewHolder.avatarImage, post.authorAvatar, avatarSize)
    }
}