package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PmUserModel
import io.pig.lkong.navigation.AppNavigation
import io.pig.lkong.ui.adapter.differ.PmUserDiffer
import io.pig.lkong.ui.adapter.item.PmUserViewHolder
import io.pig.lkong.util.DateUtil
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.SlateUtil
import io.pig.lkong.util.UiUtil
import io.pig.widget.adapter.MutableViewAdapter

class PmUserAdapter(private val context: Context) :
    MutableViewAdapter<PmUserModel>(PmUserDiffer) {

    private val todayPrefix = context.getString(R.string.text_datetime_today)
    private val avatarSize = UiUtil.getDefaultAvatarSize(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pm_user, parent, false)
        return PmUserViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val pmUser = getItem(position)
        val viewHolder = holder as PmUserViewHolder
        val text = SlateUtil.slateToText(pmUser.content)
        viewHolder.chatMsgText.text = text
        viewHolder.authorNameText.text = pmUser.authorName
        viewHolder.datelineText.text = DateUtil.formatDateByToday(
            pmUser.lastTime,
            todayPrefix
        )
        ImageLoaderUtil.loadLkongAvatar(
            context,
            viewHolder.avatarImage,
            pmUser.authorId,
            pmUser.authorAvatar,
            avatarSize
        )
        viewHolder.itemView.setOnClickListener {
            AppNavigation.openPmActivity(
                context,
                pmUser.authorId,
                pmUser.authorName,
                pmUser.authorAvatar
            )
        }
    }
}