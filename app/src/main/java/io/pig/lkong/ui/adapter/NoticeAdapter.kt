package io.pig.lkong.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.pig.lkong.R
import io.pig.lkong.model.NoticeModel
import io.pig.lkong.ui.adapter.differ.NoticeDiffer
import io.pig.lkong.ui.adapter.item.NoticeViewHolder
import io.pig.lkong.util.SlateUtil
import io.pig.ui.html.HtmlTagHandler
import io.pig.ui.html.HtmlToSpannedUtil
import io.pig.widget.adapter.MutableViewAdapter

class NoticeAdapter(
    private val context: Context,
    private val themeKey: String
) : MutableViewAdapter<NoticeModel>(NoticeDiffer) {

    private val tagHandler = HtmlTagHandler()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeViewHolder(v, themeKey)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val viewHolder = holder as NoticeViewHolder
        val notice = getItem(position)
        val htmlContent = SlateUtil.slateToHtml(notice.content)
        val contentText = HtmlToSpannedUtil.fromHtml(htmlContent, null, tagHandler)
        viewHolder.text.text = contentText
    }
}