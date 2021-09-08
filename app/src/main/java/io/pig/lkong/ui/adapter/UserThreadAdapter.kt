package io.pig.lkong.ui.adapter

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.UserThreadModel
import io.pig.lkong.ui.adapter.differ.UserThreadDiffer
import io.pig.lkong.ui.adapter.item.ThreadViewHolder
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.ThemeUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.MutableViewAdapter

class UserThreadAdapter(
    private val context: Context,
    private val userId: Long,
    private val username: String,
    private val userAvatar: String
) :
    MutableViewAdapter<UserThreadModel>(UserThreadDiffer) {

    private val todayPrefix: String = context.getString(R.string.text_datetime_today)
    private val avatarSize = UiUtil.getDefaultAvatarSize(context)
    private val colorAccent = ThemeUtil.accentColor(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_thread, parent, false)
        return ThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as ThreadViewHolder
        val model = getItem(position)
        bindThread(viewHolder, model)
    }

    fun bindThread(
        viewHolder: ThreadViewHolder,
        threadModel: UserThreadModel
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
        spannableTitle.append(threadModel.title)
        viewHolder.threadTitleText.text = spannableTitle
        viewHolder.threadAuthorNameText.text = username
        viewHolder.threadRepliesText.text = threadModel.replies.toString()
        viewHolder.threadDatetimeText.text = DateUtil.formatDateByToday(
            threadModel.dateline,
            todayPrefix
        )
        ImageLoaderUtil.loadLkongAvatar(
            context,
            viewHolder.avatarImage,
            userId,
            userAvatar,
            avatarSize
        )
    }
}