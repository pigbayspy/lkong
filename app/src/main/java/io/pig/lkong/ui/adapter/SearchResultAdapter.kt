package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.listener.ForumThreadModel
import io.pig.lkong.ui.adapter.differ.ForumThreadDiffer
import io.pig.lkong.ui.adapter.item.SearchThreadViewHolder
import io.pig.widget.adapter.MutableViewAdapter

class SearchResultAdapter(private val context: Context) : MutableViewAdapter<ForumThreadModel>(ForumThreadDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search_thread, parent, false)
        return SearchThreadViewHolder(view)
    }
}