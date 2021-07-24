package io.pig.lkong.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.ui.adapter.item.TimelineViewHolder
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.LkongUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineAdapter(
    val context: Context,
    timelines: List<TimelineModel>
) : BaseRecycleViewAdapter<TimelineModel>(timelines) {

    val avatarSize = UiUtil.getDefaultAvatarSize(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val isReply = (viewType == TYPE_REPLY)
        val view = if (isReply) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline_thread, parent, false)
        }
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as TimelineViewHolder
        val item = getItem(position)
        val isReply = (getItemViewType(position) == TYPE_REPLY)
        if (isReply) {
            bindReplyItem(viewHolder, item)
        } else {
            bindThreadItem(viewHolder, item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isQuote || !item.isThread) {
            TYPE_REPLY
        } else if (item.isThread) {
            TYPE_THREAD
        } else {
            TYPE_THREAD
        }
    }

    private fun bindThreadItem(holder: TimelineViewHolder, item: TimelineModel) {
// 用户发布主题

        // 用户发布主题
        val mainPrefixSpannable = SpannableStringBuilder()
        val createInfo: String =
            context.getString(R.string.format_timeline_create_thread, item.subject)
        mainPrefixSpannable.append(createInfo)
//        mainPrefixSpannable.setSpan(
//            ForegroundColorSpan(mTextColorSecondary),
//            0,
//            createInfo.length,
//            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        )
        mainPrefixSpannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            createInfo.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        mainPrefixSpannable.append('\n')
        val mainContent = item.message
        val mainSpannable = SpannableStringBuilder()
        mainSpannable.append(mainPrefixSpannable).append(mainContent)

        holder.messageText.text = mainSpannable
        holder.authorText.text = item.userName
        val avatarUrl = LkongUtil.generateAvatarUrl(item.userId)
        ImageLoaderUtil.loadAvatar(context, holder.authorAvatar, avatarUrl, avatarSize)
    }

    private fun bindReplyItem(holder: TimelineViewHolder, item: TimelineModel) {

    }

    companion object {
        const val TYPE_REPLY = 1
        const val TYPE_THREAD = 0
    }
}