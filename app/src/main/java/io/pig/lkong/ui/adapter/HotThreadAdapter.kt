package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.HotThreadModel
import io.pig.lkong.model.listener.OnItemThreadClickListener
import io.pig.lkong.ui.adapter.item.HotThreadViewHolder
import io.pig.lkong.util.ThemeUtil
import io.pig.ui.TextDrawable
import io.pig.widget.adapter.FixedViewAdapter

/**
 * @author yinhang
 * @since 2021/7/13
 */
class HotThreadAdapter(
    private val context: Context,
    private val listener: OnItemThreadClickListener,
    hotThreads: List<HotThreadModel>
) : FixedViewAdapter<HotThreadModel>(hotThreads) {

    private val accentColor = ThemeUtil.accentColor(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hot_thread, parent, false)
        return HotThreadViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as HotThreadViewHolder
        val hotThread = getItem(position)
        viewHolder.titleTextView.text = hotThread.title
        val textDrawable = TextDrawable.builder()
            .beginConfig()
            .textColor(accentColor)
            .endConfig() // use buildRect(String, int) for literal color value
            .buildRound((position + 1).toString(), accentColor)
        viewHolder.iconImageView.setImageDrawable(textDrawable)
        viewHolder.itemView.setOnClickListener {
            listener.onItemThreadClick(it, hotThread.tid)
        }
    }
}