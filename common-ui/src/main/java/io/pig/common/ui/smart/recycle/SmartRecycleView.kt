package io.pig.common.ui.smart.recycle

import android.content.Context
import io.pig.common.ui.util.FloatUtil.compareFloats
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewStub
import io.pig.common.ui.smart.recycle.em.LayoutManagerType
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import io.pig.common.ui.adapter.Bookends
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import io.pig.common.ui.R
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

open class SmartRecycleView : FrameLayout {

    companion object {
        private const val ITEM_LEFT_TO_LOAD_MORE = 10
    }

    private val recyclerView: RecyclerView
    private val mProgress: ViewStub

    // protected ViewStub mMoreProgress;
    private val mEmpty: ViewStub

    /**
     * @return inflated progress view or null
     */
    private var progressView: View? = null

    /**
     * @return inflated empty view or null
     */
    // protected View mMoreProgressView;
    protected var emptyView: View? = null
    protected var mClipToPadding = false
    protected var mPadding = 0
    protected var mPaddingTop = 0
    protected var mPaddingBottom = 0
    protected var mPaddingLeft = 0
    protected var mPaddingRight = 0
    protected var mScrollbarStyle = 0
    protected var mEmptyId = 0

    // protected int mMoreProgressId;
    private var layoutManagerType: LayoutManagerType? = null
    private val mInternalOnScrollListener: RecyclerView.OnScrollListener
    private var mSwipeDismissScrollListener: RecyclerView.OnScrollListener? = null
    protected var mExternalOnScrollListener: RecyclerView.OnScrollListener? = null
    protected var mOnMoreListener: OnMoreListener? = null

    /**
     * Enable/Disable the More event
     */
    private var isLoadingMore = false
    private val swipeToRefresh: SwipeRefreshLayout
    private var mSuperRecyclerViewMainLayout = 0
    private var mProgressId = 0
    private var lastScrollPositions: IntArray? = null

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(
        context, attrs
    ) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        initAttrs(attrs)
    }

    init {
        val v = LayoutInflater.from(context).inflate(mSuperRecyclerViewMainLayout, this)
        swipeToRefresh = v.findViewById(R.id.ptr_layout)
        swipeToRefresh.isEnabled = false
        mProgress = v.findViewById(android.R.id.progress)
        mProgress.layoutResource = mProgressId
        progressView = mProgress.inflate()
        mOnMoreListener?.onChangeMoreVisibility(GONE)
        mEmpty = v.findViewById(R.id.empty)
        mEmpty.layoutResource = mEmptyId
        if (mEmptyId != 0) {
            emptyView = mEmpty.inflate()
        }
        mEmpty.visibility = GONE
        val recyclerView = v.findViewById<View>(android.R.id.list)
        if (recyclerView is RecyclerView) {
            this.recyclerView = recyclerView
        } else {
            throw IllegalArgumentException("SuperRecyclerView works with a RecyclerView!")
        }
        recyclerView.clipToPadding = mClipToPadding
        mInternalOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                processOnMore()
                if (mExternalOnScrollListener != null) mExternalOnScrollListener!!.onScrolled(
                    recyclerView,
                    dx,
                    dy
                )
                if (mSwipeDismissScrollListener != null) mSwipeDismissScrollListener!!.onScrolled(
                    recyclerView,
                    dx,
                    dy
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mExternalOnScrollListener != null) mExternalOnScrollListener!!.onScrollStateChanged(
                    recyclerView,
                    newState
                )
                if (mSwipeDismissScrollListener != null) mSwipeDismissScrollListener!!.onScrollStateChanged(
                    recyclerView,
                    newState
                )
            }
        }
        recyclerView.addOnScrollListener(mInternalOnScrollListener)
        initRecyclerView()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SmartRecycleView)
        try {
            mSuperRecyclerViewMainLayout = a.getResourceId(
                R.styleable.SmartRecycleView_mainLayoutId,
                R.layout.layout_progress_recyclerview
            )
            mClipToPadding =
                a.getBoolean(R.styleable.SmartRecycleView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.SmartRecycleView_recyclerPadding, -1.0f).toInt()
            mPaddingTop =
                a.getDimension(R.styleable.SmartRecycleView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingBottom =
                a.getDimension(R.styleable.SmartRecycleView_recyclerPaddingBottom, 0.0f)
                    .toInt()
            mPaddingLeft =
                a.getDimension(R.styleable.SmartRecycleView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingRight = a.getDimension(R.styleable.SmartRecycleView_recyclerPaddingRight, 0.0f)
                .toInt()
            mScrollbarStyle = a.getInt(R.styleable.SmartRecycleView_scrollbarStyle, -1)
            mEmptyId = a.getResourceId(R.styleable.SmartRecycleView_layout_empty, 0)
            mProgressId = a.getResourceId(
                R.styleable.SmartRecycleView_layout_progress,
                R.layout.layout_progress
            )
        } finally {
            a.recycle()
        }
    }

    /**
     * Implement this method to customize the AbsListView
     */
    private fun initRecyclerView() {
        if (!compareFloats(mPadding.toFloat(), -1.0f)) {
            recyclerView.setPadding(mPadding, mPadding, mPadding, mPadding)
        } else {
            recyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
        }
        if (mScrollbarStyle != -1) {
            recyclerView.scrollBarStyle = mScrollbarStyle
        }
    }

    private fun processOnMore() {
        val layoutManager = recyclerView.layoutManager
        val lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager)
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager.itemCount
        if ((totalItemCount - lastVisibleItemPosition <= ITEM_LEFT_TO_LOAD_MORE ||
                    totalItemCount - lastVisibleItemPosition == 0 && totalItemCount > visibleItemCount)
            && !isLoadingMore
        ) {
            isLoadingMore = true
            mOnMoreListener?.apply {
                onChangeMoreVisibility(VISIBLE)
                onMoreAsked(
                    recyclerView.adapter!!.itemCount,
                    ITEM_LEFT_TO_LOAD_MORE,
                    lastVisibleItemPosition
                )
            }
        }
    }

    private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
        var lastVisibleItemPosition = -1
        if (layoutManagerType == null) {
            layoutManagerType = if (layoutManager is GridLayoutManager) {
                LayoutManagerType.GRID
            } else if (layoutManager is LinearLayoutManager) {
                LayoutManagerType.LINEAR
            } else if (layoutManager is StaggeredGridLayoutManager) {
                LayoutManagerType.STAGGERED_GRID
            } else {
                throw RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager")
            }
        }
        when (layoutManagerType) {
            LayoutManagerType.LINEAR -> lastVisibleItemPosition =
                (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
            LayoutManagerType.GRID -> lastVisibleItemPosition =
                (layoutManager as GridLayoutManager?)!!.findLastVisibleItemPosition()
            LayoutManagerType.STAGGERED_GRID -> lastVisibleItemPosition =
                caseStaggeredGrid(layoutManager)
        }
        return lastVisibleItemPosition
    }

    private fun caseStaggeredGrid(layoutManager: RecyclerView.LayoutManager?): Int {
        val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager?
        if (lastScrollPositions == null) lastScrollPositions =
            IntArray(staggeredGridLayoutManager!!.spanCount)
        staggeredGridLayoutManager!!.findLastVisibleItemPositions(lastScrollPositions)
        return findMax(lastScrollPositions!!)
    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = Int.MIN_VALUE
        for (value in lastPositions) {
            if (value > max) max = value
        }
        return max
    }

    /**
     * @param adapter                       The new adapter to set, or null to set no adapter
     * @param compatibleWithPrevious        Should be set to true if new adapter uses the same {@android.support.v7.widget.RecyclerView.ViewHolder}
     * as previous one
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views. If adapters
     * have stable ids and/or you want to animate the disappearing views, you may
     * prefer to set this to false
     */
    private fun setAdapterInternal(
        adapter: RecyclerView.Adapter<*>?, compatibleWithPrevious: Boolean,
        removeAndRecycleExistingViews: Boolean
    ) {
        if (compatibleWithPrevious) recyclerView.swapAdapter(
            adapter,
            removeAndRecycleExistingViews
        ) else recyclerView.adapter = adapter
        mProgress.visibility = GONE
        recyclerView.visibility = VISIBLE
        swipeToRefresh.isRefreshing = false
        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                update()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                update()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                update()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                update()
            }

            override fun onChanged() {
                super.onChanged()
                update()
            }

            private fun update() {
                mProgress.visibility = GONE
                mOnMoreListener?.apply {
                    onChangeMoreVisibility(GONE)
                }
                isLoadingMore = false
                swipeToRefresh.isRefreshing = false
                if (isAdapterItemEmpty(adapter)) {
                    mEmpty.visibility = if (mEmptyId != 0) VISIBLE else GONE
                } else {
                    mEmpty.visibility = GONE
                }
            }
        })
        if (mEmptyId != 0) {
            if (adapter != null) {
                if (isAdapterItemEmpty(adapter)) {
                    mEmpty.visibility = VISIBLE
                } else {
                    mEmpty.visibility = GONE
                }
            } else {
                mEmpty.visibility = VISIBLE
            }
        }
    }

    private fun isAdapterItemEmpty(adapter: RecyclerView.Adapter<*>?): Boolean {
        return if (adapter is Bookends) {
            val bookends = adapter
            bookends.itemCount - bookends.headerCount - bookends.footerCount == 0
        } else {
            adapter!!.itemCount == 0
        }
    }

    /**
     * Set the layout manager to the recycler
     */
    fun setLayoutManager(manager: RecyclerView.LayoutManager?) {
        recyclerView.layoutManager = manager
    }

    /**
     * @param adapter                       The new adapter to , or null to set no adapter.
     * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existing Views. If adapters
     * have stable ids and/or you want to animate the disappearing views, you may
     * prefer to set this to false.
     */
    fun swapAdapter(adapter: RecyclerView.Adapter<*>?, removeAndRecycleExistingViews: Boolean) {
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews)
    }

    fun setupSwipeToDismiss(listener: DismissCallback) {
        val touchListener =
            SwipeDismissRecyclerViewTouchListener(
                recyclerView, object : DismissCallback {
                    override fun canDismiss(position: Int): Boolean {
                        return listener.canDismiss(position)
                    }

                    override fun onDismiss(
                        recyclerView: RecyclerView,
                        reverseSortedPositions: IntArray
                    ) {
                        listener.onDismiss(recyclerView, reverseSortedPositions)
                    }
                })
        mSwipeDismissScrollListener = touchListener.makeScrollListener()
        recyclerView.setOnTouchListener(touchListener)
    }

    /**
     * Remove the adapter from the recycler
     */
    fun clear() {
        recyclerView.adapter = null
    }

    /**
     * Show the progressbar
     */
    fun showProgress() {
        hideRecycler()
        if (mEmptyId != 0) {
            mEmpty.visibility = INVISIBLE
        }
        mProgress.visibility = VISIBLE
    }

    /**
     * Hide the progressbar and show the recycler
     */
    fun showRecycler() {
        hideProgress()
        if (mEmptyId != 0) {
            if (recyclerView.adapter != null) {
                if (isAdapterItemEmpty(recyclerView.adapter)) {
                    mEmpty.visibility = VISIBLE
                } else {
                    mEmpty.visibility = GONE
                }
            } else {
                mEmpty.visibility = VISIBLE
            }
        }
        recyclerView.visibility = VISIBLE
    }

    fun showMoreProgress() {
        mOnMoreListener?.apply {
            onChangeMoreVisibility(VISIBLE)
        }
    }

    fun hideMoreProgress() {
        mOnMoreListener?.apply {
            onChangeMoreVisibility(GONE)
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        swipeToRefresh.isRefreshing = refreshing
    }

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     */
    fun setRefreshListener(listener: OnRefreshListener?) {
        swipeToRefresh.isEnabled = true
        swipeToRefresh.setOnRefreshListener(listener)
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     */
    fun setRefreshingColorResources(
        @ColorRes colRes1: Int,
        @ColorRes colRes2: Int,
        @ColorRes colRes3: Int,
        @ColorRes colRes4: Int
    ) {
        swipeToRefresh.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4)
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     */
    fun setRefreshingColor(col1: Int, col2: Int, col3: Int, col4: Int) {
        swipeToRefresh.setColorSchemeColors(col1, col2, col3, col4)
    }

    /**
     * Hide the progressbar
     */
    fun hideProgress() {
        mProgress.visibility = GONE
    }

    /**
     * Hide the recycler
     */
    fun hideRecycler() {
        recyclerView.visibility = GONE
    }

    /**
     * Set the scroll listener for the recycler
     */
    fun setOnScrollListener(listener: RecyclerView.OnScrollListener?) {
        mExternalOnScrollListener = listener
    }

    /**
     * Add the onItemTouchListener for the recycler
     */
    fun addOnItemTouchListener(listener: OnItemTouchListener?) {
        recyclerView.addOnItemTouchListener(listener!!)
    }

    /**
     * Remove the onItemTouchListener for the recycler
     */
    fun removeOnItemTouchListener(listener: OnItemTouchListener?) {
        recyclerView.removeOnItemTouchListener(listener!!)
    }
    /**
     * @return the recycler adapter
     */
    /**
     * Set the adapter to the recycler
     * Automatically hide the progressbar
     * Set the refresh to false
     * If adapter is empty, then the emptyview is shown
     */
    var adapter: RecyclerView.Adapter<*>?
        get() = recyclerView.adapter
        set(adapter) {
            setAdapterInternal(adapter, false, true)
        }


    fun setOnMoreListener(onMoreListener: OnMoreListener) {
        mOnMoreListener = onMoreListener
    }

    /**
     * Remove the moreListener
     */
    fun removeMoreListener() {
        mOnMoreListener = null
    }

    override fun setOnTouchListener(listener: OnTouchListener) {
        recyclerView.setOnTouchListener(listener)
    }

    fun addItemDecoration(itemDecoration: ItemDecoration?) {
        recyclerView.addItemDecoration(itemDecoration!!)
    }

    fun addItemDecoration(itemDecoration: ItemDecoration?, index: Int) {
        recyclerView.addItemDecoration(itemDecoration!!, index)
    }

    fun removeItemDecoration(itemDecoration: ItemDecoration?) {
        recyclerView.removeItemDecoration(itemDecoration!!)
    }

    /**
     * Animate a scroll by the given amount of pixels along either axis.
     *
     * @param dx Pixels to scroll horizontally
     * @param dy Pixels to scroll vertically
     */
    fun smoothScrollBy(dx: Int, dy: Int) {
        recyclerView.smoothScrollBy(dx, dy)
    }
}