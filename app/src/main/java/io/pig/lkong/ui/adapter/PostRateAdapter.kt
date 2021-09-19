package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PostModel
import io.pig.lkong.ui.adapter.item.PostRateViewHolder
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/8/18
 */
class PostRateAdapter(context: Context, rates: List<PostModel.PostRateModel>) :
    FixedViewAdapter<PostModel.PostRateModel>(rates) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_rate, parent, false)
        return PostRateViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as PostRateViewHolder
        val item = getItem(position)
        viewHolder.userNameText.text = item.username
        viewHolder.scoreText.text = item.num.toString()
        if (item.reason.isBlank()) {
            viewHolder.reasonText.visibility = View.GONE
        } else {
            viewHolder.reasonText.text = item.reason
        }
    }
}