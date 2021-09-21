package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.NoticeModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.differ.NoticeDiffer
import io.pig.lkong.ui.adapter.item.NoticeViewHolder
import io.pig.lkong.util.JsonUtil
import io.pig.widget.adapter.MutableViewAdapter

class NoticeAdapter(
    private val context: Context,
    private val themeKey: String
) : MutableViewAdapter<NoticeModel>(NoticeDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(v, themeKey)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as NoticeViewHolder
        val notice = getItem(position)
        renderContent(viewHolder, notice)
    }

    private fun renderContent(viewHolder: NoticeViewHolder, notice: NoticeModel) {
        when (notice.action) {
            "replyThread" -> {
                val reply = JsonUtil.fromJson(notice.content, ReplyThread::class.java)
                val text = if (reply.user.isEmpty()) {
                    context.getString(R.string.format_reply_thread, reply.title)
                } else {
                    val username = reply.user[0].name
                    context.getString(R.string.format_users_reply_thread, username, reply.title)
                }
                viewHolder.text.text = text
                viewHolder.card.setOnClickListener {
                    AppNavigation.openPostListActivity(context, reply.tid)
                }
            }
            "modifyThread" -> {
                val modify = JsonUtil.fromJson(notice.content, ModifyThread::class.java)
                when (modify.key) {
                    "modifyThreadLock" -> {
                        val text = context.getString(R.string.format_close_thread, modify.title)
                        viewHolder.text.text = text
                    }
                }
            }
        }
    }

    class ReplyThread(
        val title: String,
        val repnum: Int,
        val tid: Long,
        val user: List<UserReply>
    ) {
        class UserReply(val pid: String, val uid: Long, val name: String)
    }

    class ModifyThread(val key: String, val tid: Long, val args: OperateParam, val title: String) {
        class OperateParam(val tid: Long, val undo: Boolean, val reason: String)
    }
}