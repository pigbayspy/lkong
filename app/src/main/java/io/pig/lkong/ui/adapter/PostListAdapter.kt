package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.PostModel
import io.pig.lkong.ui.adapter.item.PostViewHolder
import io.pig.lkong.ui.adapter.listener.OnPostButtonClickListener
import io.pig.widget.adapter.BaseRecycleViewAdapter

/**
 * @author yinhang
 * @since 2021/6/28
 */
class PostListAdapter(
    val context: Context,
    private val listener: OnPostButtonClickListener,
    postList: List<PostModel>
) : BaseRecycleViewAdapter<PostModel>(postList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(v, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val post = getItem(position)
        // Todo
    }
}