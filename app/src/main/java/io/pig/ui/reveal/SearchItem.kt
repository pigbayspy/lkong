package io.pig.ui.reveal

import android.graphics.drawable.Drawable

class SearchItem {

    val title: String
    val value: String
    val type: Int
    var icon: Drawable? = null

    /**
     * Create a search result with text and an icon
     * @param title display value
     * @param value inner value for search
     * @param type item type
     */
    constructor(title: String, value: String, type: Int = TYPE_SEARCH_ITEM_DEFAULT) {
        this.title = title
        this.value = value
        this.type = type
    }

    companion object {
        const val TYPE_SEARCH_ITEM_HISTORY = 0
        const val TYPE_SEARCH_ITEM_SUGGESTION = 1
        const val TYPE_SEARCH_ITEM_OPTION = 2
        const val TYPE_SEARCH_ITEM_CUSTOM = 3
        const val TYPE_SEARCH_ITEM_DEFAULT = TYPE_SEARCH_ITEM_HISTORY
    }
}