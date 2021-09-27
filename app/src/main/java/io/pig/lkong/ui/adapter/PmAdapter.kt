package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PrivateMessageModel
import io.pig.lkong.ui.adapter.differ.PrivateMessageDiffer
import io.pig.lkong.ui.adapter.item.PmReceiveMsgViewHolder
import io.pig.lkong.ui.adapter.item.PmSendMsgViewHolder
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.SlateUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.MutableViewAdapter

class PmAdapter(
    private val context: Context,
    private val userId: Long,
    private val userAvatar: String?
) : MutableViewAdapter<PrivateMessageModel>(PrivateMessageDiffer) {

    private val todayPrefix = context.getString(R.string.text_datetime_today)
    private val avatarSize = UiUtil.getDefaultAvatarSize(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SEND -> {
                val view = inflater.inflate(R.layout.item_private_message_send, parent, false)
                PmSendMsgViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_private_message_receive, parent, false)
                PmReceiveMsgViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as PmSendMsgViewHolder
        val msg = getItem(position)
        viewHolder.messageText.text = SlateUtil.slateToText(msg.content)
        viewHolder.datelineText.text = DateUtil.formatDateByToday(
            msg.dateline,
            todayPrefix
        )
        if (viewHolder is PmReceiveMsgViewHolder) {
            ImageLoaderUtil.loadLkongAvatar(
                context,
                viewHolder.avatarImage,
                userId,
                userAvatar,
                avatarSize
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val model = getItem(position)
        return if (model.uid == userId) {
            TYPE_RECEIVE
        } else {
            TYPE_SEND
        }
    }

    companion object {
        private const val TYPE_SEND = 1
        private const val TYPE_RECEIVE = 2
    }
}