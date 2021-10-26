package io.pig.ui.reveal

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import io.pig.lkong.R
import io.pig.ui.button.HomeButton
import kotlin.math.ceil
import kotlin.math.cos

class PersistentSearchView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private val mCardHeight: Int
    private val mCustomToolbarHeight: Int
    private val mCardVerticalPadding: Int
    private var mCardHorizontalPadding: Int
    private val mDisplayMode: DisplayMode
    private var mSearchCardElevation: Int
    private val mHomeButtonMode: Int
    private val mIsMic: Boolean

    private var mCurrentState: SearchViewState? = null
    private var mLastState: SearchViewState? = null
    private var mAvoidTriggerTextWatcher = false

    private val mHomeButtonCloseIconState: HomeButton.IconState
    private val mHomeButtonOpenIconState: HomeButton.IconState
    private val mHomeButtonSearchIconState: HomeButton.IconState

    init {
        isSaveEnabled = true
        LayoutInflater.from(context).inflate(R.layout.layout_search_view, this, true)

        val attrsValue = context.obtainStyledAttributes(
            attrs,
            R.styleable.PersistentSearchView
        )

        mDisplayMode = DisplayMode.fromInt(
            attrsValue.getInt(
                R.styleable.PersistentSearchView_persistentSV_displayMode,
                DisplayMode.MENUITEM.toInt()
            )
        )
        mCustomToolbarHeight = attrsValue.getDimensionPixelSize(
            R.styleable.PersistentSearchView_persistentSV_customToolbarHeight,
            calculateToolbarSize(context)
        )
        val searchCardElevationValue = attrsValue.getDimensionPixelSize(
            R.styleable.PersistentSearchView_persistentSV_searchCardElevation,
            -1
        )
        mHomeButtonMode =
            attrsValue.getInt(R.styleable.PersistentSearchView_persistentSV_homeButtonMode, 0)
        mSearchCardElevation = if (searchCardElevationValue < 0) {
            getContext().resources.getDimensionPixelSize(R.dimen.search_card_default_card_elevation)
        } else {
            searchCardElevationValue
        }

        mCardHeight = resources.getDimensionPixelSize(R.dimen.search_card_height)
        mCardVerticalPadding = (mCustomToolbarHeight - mCardHeight) / 2

        when (mDisplayMode) {
            DisplayMode.TOOLBAR -> {
                if (mHomeButtonMode == 0) { // Arrow Mode
                    mHomeButtonCloseIconState = HomeButton.IconState.ARROW
                    mHomeButtonOpenIconState = HomeButton.IconState.ARROW
                } else { // Burger Mode
                    mHomeButtonCloseIconState = HomeButton.IconState.BURGER
                    mHomeButtonOpenIconState = HomeButton.IconState.ARROW
                }
                mCardHorizontalPadding =
                    resources.getDimensionPixelSize(R.dimen.search_card_visible_padding_toolbar_mode)
            }
            else -> {
                mCardHorizontalPadding =
                    resources.getDimensionPixelSize(R.dimen.search_card_visible_padding_menu_item_mode)
                if (mCardVerticalPadding > mCardHorizontalPadding) {
                    mCardHorizontalPadding = mCardVerticalPadding
                }
                mHomeButtonCloseIconState = HomeButton.IconState.ARROW
                mHomeButtonOpenIconState = HomeButton.IconState.ARROW
            }
        }
        setCurrentState(SearchViewState.NORMAL)
        mHomeButtonSearchIconState = HomeButton.IconState.ARROW

        attrsValue.recycle()

        this.mIsMic = true
    }

    private fun setCurrentState(state: SearchViewState) {
        mLastState = mCurrentState
        mCurrentState = state
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        var totalHeight = 0
        var searchCardWidth: Int
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                if (i == 0 && child is CardView) {
                    val horizontalPadding =
                        ceil(calculateHorizontalPadding(child).toDouble()).toInt()
                    val verticalPadding =
                        ceil(calculateVerticalPadding(child).toDouble())
                            .toInt()
                    val searchCardLeft = mCardHorizontalPadding - horizontalPadding
                    searchCardWidth = widthSize - searchCardLeft * 2
                    val cardWidthSpec =
                        MeasureSpec.makeMeasureSpec(searchCardWidth, MeasureSpec.EXACTLY)
                    measureChild(child, cardWidthSpec, heightMeasureSpec)
                    val childMeasuredHeight = child.getMeasuredHeight()
                    val childMeasuredWidth = child.getMeasuredWidth()
                    val childHeight = childMeasuredHeight - verticalPadding * 2
                    totalHeight += childHeight + mCardVerticalPadding * 2
                }
            }
        }
        if (totalHeight < mCustomToolbarHeight) {
            totalHeight = mCustomToolbarHeight
        }
        setMeasuredDimension(widthSize, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val searchViewWidth = r - l
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (i == 0 && child is CardView) {
                val searchCard: CardView = child
                val horizontalPadding =
                    ceil(calculateHorizontalPadding(searchCard).toDouble()).toInt()
                val verticalPadding =
                    ceil(calculateVerticalPadding(searchCard).toDouble()).toInt()
                val searchCardLeft = mCardHorizontalPadding - horizontalPadding
                val searchCardTop = mCardVerticalPadding - verticalPadding
                val searchCardWidth = searchViewWidth - searchCardLeft * 2
                val searchCardHeight = child.measuredHeight
                val searchCardRight = searchCardLeft + searchCardWidth
                val searchCardBottom = searchCardTop + searchCardHeight
                child.layout(searchCardLeft, searchCardTop, searchCardRight, searchCardBottom)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState, mCurrentState!!)
        ss.childrenStates = SparseArray()
        for (i in 0 until childCount) {
            getChildAt(i).saveHierarchyState(ss.childrenStates)
        }
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        this.mAvoidTriggerTextWatcher = true
        val ss: SavedState = state
        super.onRestoreInstanceState(ss.superState)
        for (i in 0 until childCount) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates!!)
        }
        // dispatchStateChange(ss.getCurrentSearchViewState())
        this.mAvoidTriggerTextWatcher = false
    }


    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchThawSelfOnly(container)
    }

    private enum class DisplayMode(private val mode: Int) {
        MENUITEM(0),
        TOOLBAR(1);

        fun toInt(): Int {
            return mode
        }

        companion object {
            fun fromInt(mode: Int): DisplayMode {
                for (enumMode in values()) {
                    if (enumMode.mode == mode) {
                        return enumMode
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }

    enum class SearchViewState(private val state: Int) {
        NORMAL(0), EDITING(1), SEARCH(2);

        fun toInt(): Int {
            return state
        }

        companion object {
            fun fromInt(state: Int): SearchViewState {
                for (enumState in values()) {
                    if (enumState.state == state) {
                        return enumState
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }

    class SavedState : BaseSavedState {
        var childrenStates: SparseArray<Parcelable>? = null
        private var currentSearchViewState: SearchViewState

        constructor(superState: Parcelable?, currentSearchViewState: SearchViewState) : super(
            superState
        ) {
            this.currentSearchViewState = currentSearchViewState
        }

        private constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel) {
            childrenStates = parcel.readSparseArray(classLoader)
            currentSearchViewState = SearchViewState.fromInt(parcel.readInt())
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeSparseArray(childrenStates)
            out.writeInt(currentSearchViewState.toInt())
        }

        companion object CREATOR : Parcelable.ClassLoaderCreator<SavedState> {
            override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                return SavedState(source, loader)
            }

            override fun createFromParcel(source: Parcel?): SavedState {
                return createFromParcel(null)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object {

        private val COS_45 = cos(Math.toRadians(45.0))
        private val RES_IDS_ACTION_BAR_SIZE = intArrayOf(R.attr.actionBarSize)

        fun calculateVerticalPadding(cardView: CardView): Float {
            val maxShadowSize = cardView.maxCardElevation
            val cornerRadius = cardView.radius
            val addPaddingForCorners = cardView.preventCornerOverlap
            return if (addPaddingForCorners) {
                (maxShadowSize * 1.5f + (1 - COS_45) * cornerRadius).toFloat()
            } else {
                maxShadowSize * 1.5f
            }
        }

        fun calculateHorizontalPadding(cardView: CardView): Float {
            val maxShadowSize = cardView.maxCardElevation
            val cornerRadius = cardView.radius
            val addPaddingForCorners = cardView.preventCornerOverlap
            return if (addPaddingForCorners) {
                (maxShadowSize + (1 - COS_45) * cornerRadius).toFloat()
            } else {
                maxShadowSize
            }
        }

        fun calculateToolbarSize(context: Context): Int {
            val curTheme = context.theme ?: return 0
            val att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE)
            val size = att.getDimension(0, 0f)
            att.recycle()
            return size.toInt()
        }
    }
}