package io.pig.common.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import java.util.*
import kotlin.math.abs

class Bookends(
    /**
     * Gets the base adapter that this is wrapping.
     */
    private val wrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mHeaders: MutableList<View> = ArrayList()
    private val mFooters: MutableList<View> = ArrayList()

    /**
     * Adds a header view.
     */
    fun addHeader(view: View) {
        mHeaders.add(view)
    }

    /**
     * Adds a footer view.
     */
    fun addFooter(view: View) {
        mFooters.add(view)
    }

    /**
     * Toggles the visibility of the header views.
     */
    fun setHeaderVisibility(shouldShow: Boolean) {
        for (header in mHeaders) {
            header.visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * Toggles the visibility of the footer views.
     */
    fun setFooterVisibility(shouldShow: Boolean) {
        for (footer in mFooters) {
            footer.visibility = if (shouldShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * @return the number of headers.
     */
    val headerCount: Int
        get() = mHeaders.size

    /**
     * @return the number of footers.
     */
    val footerCount: Int
        get() = mFooters.size

    /**
     * Gets the indicated header, or null if it doesn't exist.
     */
    fun getHeader(i: Int): View? {
        return if (i < mHeaders.size) mHeaders[i] else null
    }

    /**
     * Gets the indicated footer, or null if it doesn't exist.
     */
    fun getFooter(i: Int): View? {
        return if (i < mFooters.size) mFooters[i] else null
    }

    private fun isHeader(viewType: Int): Boolean {
        return viewType >= HEADER_VIEW_TYPE && viewType < HEADER_VIEW_TYPE + mHeaders.size
    }

    private fun isFooter(viewType: Int): Boolean {
        return viewType >= FOOTER_VIEW_TYPE && viewType < FOOTER_VIEW_TYPE + mFooters.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            isHeader(viewType) -> {
                val whichHeader = abs(viewType - HEADER_VIEW_TYPE)
                val headerView = mHeaders[whichHeader]
                object : RecyclerView.ViewHolder(headerView) {}
            }
            isFooter(viewType) -> {
                val whichFooter = abs(viewType - FOOTER_VIEW_TYPE)
                val footerView = mFooters[whichFooter]
                object : RecyclerView.ViewHolder(footerView) {}
            }
            else -> {
                wrappedAdapter.onCreateViewHolder(viewGroup, viewType)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position < mHeaders.size) {
            // Headers don't need anything special
        } else if (position < mHeaders.size + wrappedAdapter.itemCount) {
            // This is a real position, not a header or footer. Bind it.
            wrappedAdapter.onBindViewHolder(viewHolder, position - mHeaders.size)
        }
        // Footers don't need anything special
    }

    override fun getItemCount(): Int {
        return mHeaders.size + wrappedAdapter.itemCount + mFooters.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < mHeaders.size -> {
                HEADER_VIEW_TYPE + position
            }
            position < mHeaders.size + wrappedAdapter.itemCount -> {
                wrappedAdapter.getItemViewType(position - mHeaders.size)
            }
            else -> {
                FOOTER_VIEW_TYPE + position - mHeaders.size - wrappedAdapter.itemCount
            }
        }
    }

    companion object {
        /**
         * Defines available view type integers for headers and footers.
         *
         *
         * How this works:
         * - Regular views use view types starting from 0, counting upwards
         * - Header views use view types starting from -1000, counting upwards
         * - Footer views use view types starting from -2000, counting upwards
         *
         *
         * This means that you're safe as long as the base adapter doesn't use negative view types,
         * and as long as you have fewer than 1000 headers and footers
         */
        private const val HEADER_VIEW_TYPE = -1000
        private const val FOOTER_VIEW_TYPE = -2000
    }

    /**
     * Constructor.
     */
    init {
        wrappedAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                this@Bookends.notifyItemRangeChanged(positionStart + headerCount, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                this@Bookends.notifyItemRangeChanged(
                    positionStart + headerCount,
                    itemCount,
                    payload
                )
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                notifyItemRangeInserted(positionStart + headerCount, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                notifyItemRangeRemoved(positionStart + headerCount, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                notifyItemMoved(fromPosition + headerCount, toPosition + headerCount)
            }
        })
    }
}