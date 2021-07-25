package io.pig.lkong.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.TimelineModel
import io.pig.lkong.ui.adapter.item.TimelineReplyHolder
import io.pig.lkong.ui.adapter.item.TimelineViewHolder
import io.pig.lkong.util.*
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineAdapter(
    val context: Context,
    val themeKey: String,
    timelines: List<TimelineModel>
) : BaseRecycleViewAdapter<TimelineModel>(timelines) {

    private val avatarSize = UiUtil.getDefaultAvatarSize(context)
    private val textColorSecondary = ThemeUtil.textColorPrimary(context, themeKey)
    private val todayPrefix = context.getString(R.string.text_datetime_today)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val isReply = (viewType == TYPE_REPLY)
        return if (isReply) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline, parent, false)
            TimelineReplyHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_timeline_thread, parent, false)
            TimelineViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as TimelineViewHolder
        val item = getItem(position)
        val isReply = (getItemViewType(position) == TYPE_REPLY)
        if (isReply) {
            bindReplyItem(viewHolder as TimelineReplyHolder, item)
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
        mainPrefixSpannable.setSpan(
            ForegroundColorSpan(textColorSecondary),
            0,
            createInfo.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
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

    private fun bindReplyItem(holder: TimelineReplyHolder, item: TimelineModel) {
        val mainPrefixSpannable = SpannableStringBuilder()
        val mainContent = if (item.isQuote) {
            // 回复某一条回复
            val quote = item.replyQuote!!
            holder.secondaryContainer.visibility = View.VISIBLE
            val spanText = SpannableStringBuilder()
            val secondaryText: String = context.getString(
                R.string.format_timeline_reply_to_reply,
                quote.posterName,
                item.subject
            )
            spanText.append(secondaryText)
            if (quote.posterName.isNotEmpty()) {
                val nameStart: Int = secondaryText.indexOf(quote.posterName)
                val nameEnd: Int = nameStart + quote.posterName.length
                spanText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    nameStart,
                    nameEnd,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            holder.secondaryMessageText.text = spanText
            holder.thirdMessageText.text = quote.posterMessage
            quote.message
        } else {
            // 回复某一主题
            holder.secondaryContainer.visibility = View.VISIBLE
            val spanText = SpannableStringBuilder()
            val secondaryText: String =
                context.getString(R.string.format_timeline_reply_to_thread, item.threadAuthor)
            spanText.append(secondaryText)
            if (item.threadAuthor.isNotEmpty()) {
                val nameStart: Int = secondaryText.indexOf(item.threadAuthor)
                val nameEnd: Int = nameStart + item.threadAuthor.length
                spanText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    nameStart,
                    nameEnd,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            holder.secondaryMessageText.text = spanText
            holder.thirdMessageText.text = item.subject
            item.message
        }
        val mainSpannable = SpannableStringBuilder()
        mainSpannable.append(mainPrefixSpannable).append(mainContent)
        holder.messageText.text = mainSpannable

        holder.authorText.text = item.userName
        holder.datelineText.text = DateUtil.formatDateByToday(item.dateline, todayPrefix)
        val avatarUrl = LkongUtil.generateAvatarUrl(item.userId)
        ImageLoaderUtil.loadAvatar(
            context,
            holder.authorAvatar,
            avatarUrl,
            avatarSize
        )
    }

    companion object {
        const val TYPE_REPLY = 1
        const val TYPE_THREAD = 0
    }
}