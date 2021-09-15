package io.pig.ui.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.widget.adapter.FixedViewAdapter
import io.pig.widget.listener.RecycleViewClickListener
import java.io.IOException

class EmoticonAdapter(
    private val context: Context,
    emojis: List<String>,
    private val listener: RecycleViewClickListener
) : FixedViewAdapter<String>(
    emojis
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emoticon, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as ViewHolder
        val emoticonPath = getItem(position)
        try {
            val d = Drawable.createFromStream(context.assets.open("emoji/$emoticonPath"), null)
            viewHolder.emoticonImage.setImageDrawable(d)
            viewHolder.itemView.setOnClickListener { view: View ->
                listener.onItemClick(view, position, getItemId(position))
            }
        } catch (e: IOException) {
            viewHolder.emoticonImage.setImageResource(R.drawable.placeholder_error)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        val emoticonImage: ImageView = v.findViewById(R.id.item_emoticon_image)
    }
}