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
import io.pig.lkong.ui.adapter.differ.TimelineDiffer
import io.pig.lkong.ui.adapter.item.TimelineReplyHolder
import io.pig.lkong.ui.adapter.item.TimelineViewHolder
import io.pig.lkong.ui.adapter.listener.OnTimelineClickListener
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.MutableViewAdapter

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineAdapter(
    private val context: Context,
    private val listener: OnTimelineClickListener,
    themeKey: String,
) : MutableViewAdapter<TimelineModel>(TimelineDiffer) {

    private val avatarSize = UiUtil.getDefaultAvatarSize(context)
    private val textColorSecondary = ThemeUtil.textColorPrimary(context, themeKey)
    private val todayPrefix = context.getString(R.string.text_datetime_today)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_QUOTE, TYPE_REPLY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_timeline, parent, false)
                TimelineReplyHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_timeline_thread, parent, false)
                TimelineViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as TimelineViewHolder
        val item = getItem(position)
        when (getItemViewType(position)) {
            TYPE_QUOTE -> bindQuoteItem(viewHolder as TimelineReplyHolder, item)
            TYPE_REPLY -> bindReplyItem(viewHolder as TimelineReplyHolder, item)
            TYPE_THREAD -> bindThreadItem(viewHolder, item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            item.quoteInfo != null -> {
                TYPE_QUOTE
            }
            item.threadInfo != null -> {
                TYPE_THREAD
            }
            else -> {
                TYPE_REPLY
            }
        }
    }

    private fun bindThreadItem(holder: TimelineViewHolder, item: TimelineModel) {
        // 用户发布主题
        val mainPrefixSpannable = SpannableStringBuilder()
        val createInfo: String =
            context.getString(R.string.format_timeline_thread, item.threadInfo!!.title)
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
        val mainContent = item.content
        val mainSpannable = SpannableStringBuilder()
        mainSpannable.append(mainPrefixSpannable).append(mainContent)

        holder.messageText.text = mainSpannable
        holder.authorText.text = item.authorName
        ImageLoaderUtil.loadLkongAvatar(
            context, holder.authorAvatar, item.authorId,
            item.authorAvatar, avatarSize
        )
        holder.authorAvatar.setOnClickListener {
            listener.onProfileAreaClick(it, item.authorId)
        }
    }

    private fun bindQuoteItem(holder: TimelineReplyHolder, item: TimelineModel) {
        val mainPrefixSpannable = SpannableStringBuilder()
        val quote = item.quoteInfo!!
        holder.secondaryContainer.visibility = View.VISIBLE
        val spanText = SpannableStringBuilder()
        val secondaryText: String = context.getString(
            R.string.format_timeline_quote,
            item.authorName,
            quote.authorName
        )
        spanText.append(secondaryText)
        if (quote.authorName.isNotEmpty()) {
            val nameStart: Int = secondaryText.indexOf(quote.authorName)
            val nameEnd: Int = nameStart + quote.authorName.length
            spanText.setSpan(
                StyleSpan(Typeface.BOLD),
                nameStart,
                nameEnd,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        holder.secondaryMessageText.text = spanText
        holder.thirdMessageText.text = quote.content
        val mainContent = item.content
        val mainSpannable = SpannableStringBuilder()
        mainSpannable.append(mainPrefixSpannable).append(mainContent)
        holder.messageText.text = mainSpannable

        holder.authorText.text = item.authorName
        holder.datelineText.text = DateUtil.formatDateByToday(item.dateline, todayPrefix)
        ImageLoaderUtil.loadLkongAvatar(
            context,
            holder.authorAvatar,
            item.authorId,
            item.authorAvatar,
            avatarSize
        )
        holder.authorAvatar.setOnClickListener {
            listener.onProfileAreaClick(it, item.authorId)
        }
    }

    private fun bindReplyItem(holder: TimelineReplyHolder, item: TimelineModel) {
        val mainPrefixSpannable = SpannableStringBuilder()

        // 回复某一主题
        holder.secondaryContainer.visibility = View.VISIBLE
        val spanText = SpannableStringBuilder()
        val threadAuthorName = item.replyInfo!!.authorName
        val secondaryText: String =
            context.getString(R.string.format_timeline_reply, threadAuthorName)
        spanText.append(secondaryText)
        val nameStart: Int = secondaryText.indexOf(threadAuthorName)
        val nameEnd: Int = nameStart + threadAuthorName.length
        spanText.setSpan(
            StyleSpan(Typeface.BOLD),
            nameStart,
            nameEnd,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        holder.secondaryMessageText.text = spanText
        val replyTitle = SpannableStringBuilder()
        replyTitle.append(item.replyInfo.title)
        replyTitle.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            item.replyInfo.title.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        holder.thirdMessageText.text = replyTitle
        val mainContent = item.content

        val mainSpannable = SpannableStringBuilder()
        mainSpannable.append(mainPrefixSpannable).append(mainContent)
        holder.messageText.text = mainSpannable

        holder.authorText.text = item.authorName
        holder.datelineText.text = DateUtil.formatDateByToday(item.dateline, todayPrefix)
        ImageLoaderUtil.loadLkongAvatar(
            context,
            holder.authorAvatar,
            item.authorId,
            item.authorAvatar,
            avatarSize
        )
        holder.authorAvatar.setOnClickListener {
            listener.onProfileAreaClick(it, item.authorId)
        }
    }

    companion object {
        const val TYPE_QUOTE = 2
        const val TYPE_REPLY = 1
        const val TYPE_THREAD = 0
    }
}