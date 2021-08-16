package io.pig.lkong.ui.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.FavoriteThreadModel
import io.pig.lkong.ui.adapter.item.ThreadViewHolder
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/8/15
 */
class FavoriteAdapter(
    private val context: Context,
    private val listener: OnThreadClickListener,
    threads: List<FavoriteThreadModel>
) :
    FixedViewAdapter<FavoriteThreadModel>(threads) {

    private val todayPrefix: String
    private val avatarSize: Int

    init {
        this.todayPrefix = context.getString(R.string.text_datetime_today)
        this.avatarSize = UiUtil.getDefaultAvatarSize(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thread, parent, false)
        return ThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ThreadViewHolder
        bindThreadModel(viewHolder, position)
    }

    private fun bindThreadModel(viewHolder: ThreadViewHolder, position: Int) {
        val threadModel = getItem(position)
        val spannableTitle = SpannableStringBuilder()
        spannableTitle.append(threadModel.title)
        viewHolder.threadTitleText.text = spannableTitle
        viewHolder.threadAuthorNameText.text = threadModel.authorName
        viewHolder.threadRepliesText.text = threadModel.replyCount.toString()
        viewHolder.threadDatetimeText.text = DateUtil.formatDateByToday(
            threadModel.dateline,
            todayPrefix
        )
        ImageLoaderUtil.loadLkongAvatar(
            context,
            viewHolder.avatarImage,
            threadModel.authorId,
            threadModel.authorAvatar,
            avatarSize
        )
        viewHolder.itemView.setOnClickListener {
            listener.onItemThreadClick(it, threadModel.tid)
        }
        viewHolder.avatarImage.setOnClickListener {
            listener.onProfileAreaClick(it, threadModel.authorId)
        }
    }
}