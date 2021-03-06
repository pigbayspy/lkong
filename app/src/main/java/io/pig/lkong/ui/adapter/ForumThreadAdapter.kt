package io.pig.lkong.ui.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.ui.adapter.differ.ForumThreadDiffer
import io.pig.lkong.ui.adapter.item.ThreadViewHolder
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.MutableViewAdapter

/**
 * @author yinhang
 * @since 2021/8/15
 */
class ForumThreadAdapter(
    private val context: Context,
    private val listener: OnThreadClickListener,
) : MutableViewAdapter<ForumThreadModel>(ForumThreadDiffer) {

    private val todayPrefix: String = context.getString(R.string.text_datetime_today)
    private val avatarSize = UiUtil.getDefaultAvatarSize(context)
    private val colorAccent = ThemeUtil.accentColor(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thread, parent, false)
        return ThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ThreadViewHolder
        bindThreadModel(viewHolder, position)
    }

    private fun bindThreadModel(holder: ThreadViewHolder, position: Int) {
        val thread = getItem(position)
        val spannableTitle = SpannableStringBuilder()
        if (thread.digest) {
            val digestIndicator: String = context.getString(R.string.indicator_thread_digest)
            spannableTitle.append(digestIndicator)
            spannableTitle.setSpan(
                ForegroundColorSpan(colorAccent),
                0,
                digestIndicator.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        spannableTitle.append(thread.title)
        holder.threadTitleText.text = spannableTitle
        holder.threadAuthorNameText.text = thread.authorName
        holder.threadRepliesText.text = thread.replies.toString()
        holder.threadDatetimeText.text = DateUtil.formatDateByToday(
            thread.dateline,
            todayPrefix
        )
        ImageLoaderUtil.loadLkongAvatar(
            context,
            holder.avatarImage,
            thread.authorId,
            thread.authorAvatar,
            avatarSize
        )
        // ??????????????????
        holder.avatarImage.setOnClickListener {
            listener.onProfileAreaClick(it, thread.authorId)
        }
        holder.threadTitleText.setOnClickListener {
            listener.onItemThreadClick(it, thread.tid)
        }
    }
}