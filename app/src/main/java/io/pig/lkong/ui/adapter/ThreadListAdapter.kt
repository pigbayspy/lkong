package io.pig.lkong.ui.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.ThreadModel
import io.pig.lkong.ui.adapter.item.ThreadViewHolder
import io.pig.lkong.ui.adapter.listener.OnThreadClickListener
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * 帖子列表适配器
 *
 * @author yinhang
 * @since 2021/6/8
 */
class ThreadListAdapter(
    val context: Context,
    val listener: OnThreadClickListener,
    threads: List<ThreadModel>
) : BaseRecycleViewAdapter<ThreadModel>(threads) {

    private val colorAccent: Int
    private val todayPrefix: String
    private val avatarSize: Int

    init {
        this.colorAccent = ThemeUtil.accentColor(context)
        this.todayPrefix = context.getString(R.string.text_datetime_today)
        this.avatarSize = UiUtil.getDefaultAvatarSize(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thread, parent, false)
        return ThreadViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ThreadViewHolder = holder as ThreadViewHolder
        val threadModel: ThreadModel = getItem(position)
        bindThreadModel(viewHolder, threadModel)
    }

    private fun bindThreadModel(
        viewHolder: ThreadViewHolder,
        threadModel: ThreadModel
    ) {
        val spannableTitle = SpannableStringBuilder()
        if (threadModel.digest) {
            val digestIndicator: String = context.getString(R.string.indicator_thread_digest)
            spannableTitle.append(digestIndicator)
            spannableTitle.setSpan(
                ForegroundColorSpan(colorAccent),
                0,
                digestIndicator.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        spannableTitle.append(threadModel.subject)
        viewHolder.threadTitleView.text = spannableTitle
        viewHolder.threadUserNameView.text = threadModel.userName
        viewHolder.threadReplyCountView.text = threadModel.replyCount.toString()
        viewHolder.threadDatetimeView.text = DateUtil.formatDateByToday(
            threadModel.dateline!!,
            todayPrefix
        )
        ImageLoaderUtil.loadAvatar(
            context,
            viewHolder.avatarView,
            threadModel.userIcon,
            avatarSize
        )
    }
}