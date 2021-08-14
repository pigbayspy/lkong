package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.ForumModel
import io.pig.lkong.ui.adapter.item.ForumViewHolder
import io.pig.lkong.util.ImageLoaderUtil
import io.pig.lkong.util.NumberFormatUtil
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/7/10
 */
class ForumListAdapter(
    val context: Context,
    private val showInGrid: Boolean,
    forums: List<ForumModel>
) : FixedViewAdapter<ForumModel>(forums) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (showInGrid) {
            R.layout.item_forum_grid
        } else {
            R.layout.item_forum_list
        }
        val v: View = LayoutInflater.from(parent.context)
            .inflate(
                layout,
                parent,
                false
            )
        return ForumViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as ForumViewHolder
        val forum = getItem(position)
        viewHolder.forumTitleText.text = forum.name
        if (!showInGrid) {
            val num = NumberFormatUtil.numberToTenKiloString(
                forum.num,
                context.getString(R.string.format_unit_ten_kilo),
                keepSpace = false,
                keepIfLess = true
            )
            val secondaryInfo = context.getString(R.string.format_forum_item_summary, num)
            viewHolder.forumSecondaryText.text = secondaryInfo
        }
        ImageLoaderUtil.loadForumIcon(context, viewHolder.forumIconImage, forum.icon)
    }
}