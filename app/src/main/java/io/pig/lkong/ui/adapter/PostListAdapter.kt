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
import io.pig.lkong.util.SlateUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostListAdapter(
    val context: Context,
    val userId: Long,
    private val listener: OnPostButtonClickListener,
    postList: List<PostModel>
) : FixedViewAdapter<PostModel>(postList) {

    private val avatarSize: Int = UiUtil.getDefaultAvatarSize(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as PostViewHolder
        val post = getItem(position)
        if (post.rateSum != 0) {
            viewHolder.rateText.apply {
                visibility = View.VISIBLE
                text = context.getString(R.string.format_post_rate_summary, post.rateSum)
                setOnClickListener {
                    listener.onRateTextClick(viewHolder.itemView, post.rates)
                }
            }
        }
        if (post.authorId == userId) {
            viewHolder.editButton.visibility = View.VISIBLE
        }
        ImageLoaderUtil.loadLkongAvatar(
            context,
            viewHolder.avatarImage,
            post.authorId,
            post.authorAvatar,
            avatarSize
        )
        viewHolder.postItem.text = SlateUtil.slateToText(post.message)

        // add listener
        viewHolder.avatarImage.setOnClickListener {
            listener.onProfileImageClick(viewHolder.itemView, post.authorId)
        }
    }
}