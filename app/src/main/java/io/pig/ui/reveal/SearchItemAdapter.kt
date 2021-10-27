package io.pig.ui.reveal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import io.pig.lkong.R

class SearchItemAdapter(context: Context, options: List<SearchItem>) :
    ArrayAdapter<SearchItem>(context, 0, options) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val searchItem = getItem(position)!!
        val extraConvertView = convertView
            ?: LayoutInflater.from(context).inflate(
                R.layout.item_search_result, parent, false
            )
        val border = extraConvertView.findViewById<View>(R.id.view_border)
        if (position == 0) {
            border.visibility = View.VISIBLE
        } else {
            border.visibility = View.GONE
        }
        val title: TextView = extraConvertView.findViewById(R.id.search_text_title)
        title.text = searchItem.title
        val icon: ImageView = extraConvertView.findViewById(R.id.search_image_icon)
        if (searchItem.icon == null) {
            when (searchItem.type) {
                SearchItem.TYPE_SEARCH_ITEM_HISTORY -> icon.setImageResource(R.drawable.ic_history_black)
                SearchItem.TYPE_SEARCH_ITEM_SUGGESTION -> icon.setImageResource(R.drawable.ic_search_black)
                else -> icon.setImageResource(R.drawable.ic_search_black)
            }
        } else {
            icon.setImageDrawable(searchItem.icon)
        }
        return extraConvertView
    }
}