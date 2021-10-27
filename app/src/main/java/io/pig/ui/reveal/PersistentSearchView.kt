package io.pig.ui.reveal

import android.animation.Animator
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView.OnEditorActionListener
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import io.pig.lkong.R
import io.pig.ui.button.HomeButton
import io.pig.ui.reveal.logo.LogoView
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max

class PersistentSearchView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private val mCardHeight: Int
    private val mCustomToolbarHeight: Int
    private val mCardVerticalPadding: Int
    private var mCardHorizontalPadding: Int
    private val mDisplayMode: DisplayMode
    private var mSearchCardElevation: Int
    private val mHomeButtonMode: Int
    private val mLogoDrawable: Drawable?
    private val mArrowButtonColor: Int
    private val mSearchEditTextColor: Int
    private val mSearchEditTextHint: String
    private val mSearchEditTextHintColor: Int
    private val mStringLogoDrawable: String?
    private val mSearchTextColor: Int

    private var mIsMic: Boolean
    private var mFromX: Int = 0
    private var mFromY: Int = 0
    private var mDesireRevealWidth: Int = 0

    private var mSearchListener: SearchListener? = null
    private var mHomeButtonListener: HomeButtonListener? = null

    private var mCurrentState: SearchViewState? = null
    private var mLastState: SearchViewState? = null
    private var mAvoidTriggerTextWatcher = false
    private var showCustomKeyboard = false
    private var mVoiceRecognitionDelegate: VoiceRecognitionDelegate? = null
    private var mSuggestionBuilder: SearchSuggestionsBuilder? = null
    private var mCustomKeyboardView: KeyboardView? = null

    private val mSearchItemAdapter: SearchItemAdapter
    private val mSearchSuggestions = mutableListOf<SearchItem>()

    private val mHomeButtonCloseIconState: HomeButton.IconState
    private val mHomeButtonOpenIconState: HomeButton.IconState
    private val mHomeButtonSearchIconState: HomeButton.IconState

    private val mHomeButton: HomeButton
    private val mLogoView: LogoView
    private val mSearchEditText: EditText
    private val mSearchCardView: CardView
    private val mMicButton: ImageView
    private val mSuggestionListView: ListView

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
        mSearchTextColor = attrsValue.getColor(
            R.styleable.PersistentSearchView_persistentSV_searchTextColor,
            Color.BLACK
        )
        mLogoDrawable =
            attrsValue.getDrawable(R.styleable.PersistentSearchView_persistentSV_logoDrawable)
        mArrowButtonColor = attrsValue.getColor(
            R.styleable.PersistentSearchView_persistentSV_homeButtonColor,
            Color.BLACK
        )
        mSearchEditTextColor = attrsValue.getColor(
            R.styleable.PersistentSearchView_persistentSV_editTextColor,
            Color.BLACK
        )
        mSearchEditTextHint =
            attrsValue.getString(R.styleable.PersistentSearchView_persistentSV_editHintText) ?: ""
        mSearchEditTextHintColor = attrsValue.getColor(
            R.styleable.PersistentSearchView_persistentSV_editHintTextColor,
            Color.BLACK
        )
        mStringLogoDrawable =
            attrsValue.getString(R.styleable.PersistentSearchView_persistentSV_logoString)
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

        // init view
        mHomeButton = findViewById(R.id.search_view_button_home)
        mLogoView = findViewById(R.id.search_view_logo)
        mSearchEditText = findViewById(R.id.search_view_edit_search)
        mSearchCardView = findViewById(R.id.search_view_card_search)
        mMicButton = findViewById(R.id.search_view_button_mic)
        mSuggestionListView = findViewById(R.id.search_view_list_suggestions)

        // set value to views
        mSearchCardView.cardElevation = mSearchCardElevation.toFloat()
        mSearchCardView.maxCardElevation = mSearchCardElevation.toFloat()
        mHomeButton.setArrowDrawableColor(mArrowButtonColor)
        mHomeButton.setState(mHomeButtonCloseIconState)
        mHomeButton.setAnimationDuration(DURATION_HOME_BUTTON)
        mSearchEditText.setTextColor(mSearchEditTextColor)
        mSearchEditText.hint = mSearchEditTextHint
        mSearchEditText.setHintTextColor(mSearchEditTextHintColor)
        if (mLogoDrawable != null) {
            mLogoView.setLogo(mLogoDrawable)
        } else if (mStringLogoDrawable != null) {
            mLogoView.setLogo(mStringLogoDrawable)
        }
        mLogoView.setTextColor(mSearchTextColor)

        //
        mSearchItemAdapter = SearchItemAdapter(getContext(), mSearchSuggestions)
        mSuggestionListView.adapter = mSearchItemAdapter

        // setUpLayoutTransition
        val layoutTransition = LayoutTransition()
        layoutTransition.setDuration(DURATION_LAYOUT_TRANSITION)
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)
        layoutTransition.setStartDelay(LayoutTransition.CHANGING, 0)

        layoutTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0)
        mSearchCardView.layoutTransition = layoutTransition

        // set listener
        mHomeButton.setOnClickListener {
            when (mCurrentState) {
                SearchViewState.EDITING -> {
                    cancelEditing()
                }
                SearchViewState.SEARCH -> {
                    fromSearchToNormal()
                }
                else -> {
                    mHomeButtonListener?.onHomeButtonClick()
                }
            }
        }
        mLogoView.setOnClickListener {
            dispatchStateChange(SearchViewState.EDITING) // This would call when state is wrong.
        }
        mSearchEditText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                clearSuggestions()
                fromEditingToSearch(forceSearch = true, avoidSearch = false)
                return@OnEditorActionListener true
            }
            false
        })
        mSearchEditText.setOnKeyListener(OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                clearSuggestions()
                fromEditingToSearch(forceSearch = true, avoidSearch = false)
                return@OnKeyListener true
            } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener mSearchListener != null && mSearchListener!!.onSearchEditBackPressed()
            }
            false
        })
        micStateChanged()
        mMicButton.setOnClickListener {
            micClick()
        }
        mSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (!mAvoidTriggerTextWatcher) {
                    if (s.isNotEmpty()) {
                        showClearButton()
                        buildSearchSuggestions(getSearchText())
                    } else {
                        showMicButton()
                        buildEmptySearchSuggestions()
                    }
                }
                mSearchListener?.onSearchTermChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun cancelEditing() {
        if (TextUtils.isEmpty(mLogoView.text)) {
            fromEditingToNormal()
        } else {
            fromEditingToSearch(true)
        }
    }

    private fun micClick() {
        if (!mIsMic) {
            setSearchString("", false)
        } else {
            mVoiceRecognitionDelegate?.onStartVoiceRecognition()
        }
    }

    private fun clearSuggestions() {
        mSearchItemAdapter.clear()
    }

    fun setStartPositionFromMenuItem(menuItemView: View) {
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        setStartPositionFromMenuItem(menuItemView, width)
    }

    private fun setStartPositionFromMenuItem(menuItemView: View, desireRevealWidth: Int) {
        val location = IntArray(2)
        menuItemView.getLocationInWindow(location)
        val menuItemWidth = menuItemView.width
        mFromX = location[0] + menuItemWidth / 2
        mFromY = location[1]
        this.mDesireRevealWidth = desireRevealWidth
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

    private fun getSearchText(): String {
        return mSearchEditText.text.toString()
    }

    private fun search() {
        val searchTerm: String = getSearchText()
        if (!TextUtils.isEmpty(searchTerm)) {
            setLogoTextInt(searchTerm)
            mSearchListener?.onSearch(searchTerm)
        }
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
        dispatchStateChange(ss.getCurrentSearchViewState())
        this.mAvoidTriggerTextWatcher = false
    }

    private fun fromNormalToSearch() {
        if (mDisplayMode == DisplayMode.TOOLBAR) {
            setCurrentState(SearchViewState.SEARCH)
            search()
        } else if (mDisplayMode == DisplayMode.MENUITEM) {
            visibility = VISIBLE
            fromEditingToSearch()
        }
        mHomeButton.animateState(mHomeButtonSearchIconState)
    }

    private fun fromSearchToNormal() {
        setLogoTextInt("")
        setSearchString("", true)
        setCurrentState(SearchViewState.NORMAL)
        if (mDisplayMode == DisplayMode.TOOLBAR) {
            closeSearchInternal()
        } else if (mDisplayMode == DisplayMode.MENUITEM) {
            hideCircularlyToMenuItem()
        }
        setLogoTextInt("")
        mSearchListener?.onSearchExit()
        mHomeButton.animateState(mHomeButtonCloseIconState)
    }

    private fun fromSearchToEditing() {
        openSearchInternal(true)
        setCurrentState(SearchViewState.EDITING)
        mHomeButton.animateState(mHomeButtonOpenIconState)
    }

    private fun fromEditingToNormal() {
        setCurrentState(SearchViewState.NORMAL)
        if (mDisplayMode == DisplayMode.TOOLBAR) {
            setSearchString("", false)
            closeSearchInternal()
        } else if (mDisplayMode == DisplayMode.MENUITEM) {
            setSearchString("", false)
            hideCircularlyToMenuItem()
        }
        setLogoTextInt("")
        mSearchListener?.onSearchExit()
        mHomeButton.animateState(mHomeButtonCloseIconState)
    }

    private fun fromEditingToSearch() {
        fromEditingToSearch(forceSearch = false, avoidSearch = false)
    }

    private fun fromEditingToSearch(avoidSearch: Boolean) {
        fromEditingToSearch(false, avoidSearch)
    }

    private fun fromEditingToSearch(forceSearch: Boolean, avoidSearch: Boolean) {
        if (TextUtils.isEmpty(getSearchText())) {
            fromEditingToNormal()
        } else {
            setCurrentState(SearchViewState.SEARCH)
            if ((getSearchText() != mLogoView.text || forceSearch) && !avoidSearch) {
                search()
            }
            closeSearchInternal()
            mHomeButton.animateState(mHomeButtonSearchIconState)
        }
    }

    private fun dispatchStateChange(targetState: SearchViewState) {
        if (targetState == SearchViewState.NORMAL) {
            if (mCurrentState == SearchViewState.EDITING) {
                fromEditingToNormal()
            } else if (mCurrentState == SearchViewState.SEARCH) {
                fromSearchToNormal()
            }
        } else if (targetState == SearchViewState.EDITING) {
            if (mCurrentState == SearchViewState.NORMAL) {
                fromNormalToEditing()
            } else if (mCurrentState == SearchViewState.SEARCH) {
                fromSearchToEditing()
            }
        } else if (targetState == SearchViewState.SEARCH) {
            if (mCurrentState == SearchViewState.NORMAL) {
                fromNormalToSearch()
            } else if (mCurrentState == SearchViewState.EDITING) {
                fromEditingToSearch()
            }
        }
    }

    private fun showClearButton() {
        micStateChanged(false)
        mMicButton.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_action_clear_black, null)
        )
    }

    private fun buildSearchSuggestions(query: String) {
        mSuggestionBuilder?.let {
            mSearchSuggestions.clear()
            val suggestions: Collection<SearchItem> = it.buildSearchSuggestion(10, query)
            if (suggestions.isNotEmpty()) {
                mSearchSuggestions.addAll(suggestions)
            }
            mSearchItemAdapter.notifyDataSetChanged()
        }
    }

    private fun buildEmptySearchSuggestions() {
        mSuggestionBuilder?.let {
            mSearchSuggestions.clear()
            val suggestions: Collection<SearchItem> = it.buildEmptySearchSuggestion(10)
            if (suggestions.isNotEmpty()) {
                mSearchSuggestions.addAll(suggestions)
            }
            mSearchItemAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun openSearchInternal(openKeyboard: Boolean) {
        mLogoView.visibility = GONE
        mSearchEditText.visibility = VISIBLE
        mSearchEditText.requestFocus()
        mSuggestionListView.visibility = VISIBLE
        mSuggestionListView.onItemClickListener =
            OnItemClickListener { _, _, pos, _ ->
                hideKeyboard()
                val result: SearchItem = mSearchSuggestions[pos]
                mSearchListener.let {
                    if (it != null) {
                        if (it.onSuggestion(result)) {
                            setSearchString(result.value, true)
                            fromEditingToSearch(forceSearch = true, avoidSearch = false)
                        }
                    } else {
                        setSearchString(result.value, true)
                        fromEditingToSearch(forceSearch = true, avoidSearch = false)
                    }
                }
            }
        val currentSearchText = getSearchText()
        if (currentSearchText.isNotEmpty()) {
            buildSearchSuggestions(currentSearchText)
        } else {
            buildEmptySearchSuggestions()
        }
        mSearchListener?.onSearchEditOpened()
        if (getSearchText().isNotEmpty()) {
            showClearButton()
        }
        if (openKeyboard) {
            if (showCustomKeyboard) {
                // Show custom keyboard
                mCustomKeyboardView?.let {
                    it.visibility = VISIBLE
                    it.isEnabled = true
                    // Enable cursor, but still prevent default keyboard from showing up
                    mSearchEditText.setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                it.visibility = VISIBLE
                                it.isEnabled = true
                                val layout = (v as EditText).layout
                                val x = event.x + mSearchEditText.scrollX
                                val offset = layout.getOffsetForHorizontal(0, x)
                                if (offset > 0) {
                                    if (x > layout.getLineMax(0)) mSearchEditText.setSelection(
                                        offset
                                    ) else {
                                        // Touch was at the end of the text
                                        mSearchEditText.setSelection(offset - 1)
                                    }
                                }
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val layout = (v as EditText).layout
                                x = event.x + mSearchEditText.scrollX
                                val offset = layout.getOffsetForHorizontal(0, x)
                                if (offset > 0) {
                                    if (x > layout.getLineMax(0)) mSearchEditText.setSelection(
                                        offset
                                    ) else {
                                        // Touch point was at the end of the text
                                        mSearchEditText.setSelection(offset - 1)
                                    }
                                }
                            }
                        }
                        true
                    }
                }
            } else {
                // Show default keyboard
                mSearchEditText.setOnTouchListener(null)
                val inputMethodManager = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInputFromWindow(
                    applicationWindowToken,
                    InputMethodManager.SHOW_FORCED, 0
                )
            }
        }
    }

    private fun fromNormalToEditing() {
        if (mDisplayMode == DisplayMode.TOOLBAR) {
            setCurrentState(SearchViewState.EDITING)
            openSearchInternal(true)
        } else if (mDisplayMode == DisplayMode.MENUITEM) {
            setCurrentState(SearchViewState.EDITING)
            if (ViewCompat.isAttachedToWindow(this)) {
                revealFromMenuItem()
            } else {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        revealFromMenuItem()
                    }
                })
            }
        }
        mHomeButton.animateState(mHomeButtonOpenIconState)
    }

    private fun revealFromMenuItem() {
        visibility = VISIBLE
        revealFrom(mFromX.toFloat(), mFromY.toFloat(), mDesireRevealWidth)
    }

    private fun revealFrom(x: Float, y: Float, desireRevealWidth: Int) {
        val r = resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 96f,
            r.displayMetrics
        )
        val extraDesireRevealWidth = if (desireRevealWidth <= 0) {
            if (measuredWidth <= 0) {
                val metrics = resources.displayMetrics
                metrics.widthPixels
            } else {
                measuredWidth
            }
        } else {
            desireRevealWidth
        }
        val extraX = if (x <= 0) {
            (extraDesireRevealWidth - mCardHeight / 2).toFloat()
        } else {
            x
        }
        val extraY = if (y <= 0) {
            (mCardHeight / 2).toFloat()
        } else {
            y
        }
        val measuredHeight = measuredWidth
        val finalRadius =
            max(max(measuredHeight.toFloat(), px), extraDesireRevealWidth.toFloat()).toInt()
        val animator = ViewAnimationUtils.createCircularReveal(
            mSearchCardView, extraX.toInt(), extraY.toInt(), 0F, finalRadius.toFloat()
        )
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = DURATION_REVEAL_OPEN
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                // show search view here
                openSearchInternal(true)
            }

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animator.start()
    }

    private fun setLogoTextInt(text: String) {
        mLogoView.text = text
    }

    private fun setSearchString(text: String, avoidTriggerTextWatcher: Boolean) {
        if (avoidTriggerTextWatcher) {
            mAvoidTriggerTextWatcher = true
        }
        mSearchEditText.setText("")
        mSearchEditText.append(text)
        mAvoidTriggerTextWatcher = false
    }

    private fun hideCircularlyToMenuItem() {
        if (mFromX == 0 || mFromY == 0) {
            mFromX = right
            mFromY = top
        }
        hideCircularly(mFromX, mFromY)
    }

    private fun hideCircularly(x: Int, y: Int) {
        val r = resources
        val px: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 96f,
            r.displayMetrics
        )
        val finalRadius = max(this.measuredWidth * 1.5F, px)
        val animator =
            ViewAnimationUtils.createCircularReveal(mSearchCardView, x, y, 0F, finalRadius)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = DURATION_REVEAL_CLOSE
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                onAnimationStart(animation, true)
            }

            override fun onAnimationEnd(animation: Animator?) {
                visibility = GONE
                closeSearchInternal()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    private fun showMicButton() {
        micStateChanged(true)
        mMicButton.setImageDrawable(
            ResourcesCompat.getDrawable(resources, R.drawable.ic_action_mic_black, null)
        )
    }

    private fun micStateChanged(isMic: Boolean) {
        mIsMic = isMic
        micStateChanged()
    }

    private fun micStateChanged() {
        mMicButton.visibility = if (!mIsMic || isMicEnabled()) {
            VISIBLE
        } else {
            INVISIBLE
        }
    }

    private fun isMicEnabled(): Boolean {
        return mVoiceRecognitionDelegate != null
    }

    private fun closeSearchInternal() {
        mLogoView.visibility = VISIBLE
        mSearchEditText.visibility = GONE
        mSuggestionListView.visibility = GONE
        mSearchListener?.onSearchEditClosed()
        showMicButton()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        if (showCustomKeyboard) {
            mCustomKeyboardView?.apply {
                visibility = GONE
                isEnabled = false
            }
        } else {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                applicationWindowToken, 0
            )
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable?>?) {
        dispatchThawSelfOnly(container)
    }

    class SavedState : BaseSavedState {
        var childrenStates: SparseArray<Parcelable>? = null
        private var currentSearchViewState: SearchViewState

        constructor(superState: Parcelable?, currentSearchViewState: SearchViewState) : super(
            superState
        ) {
            this.currentSearchViewState = currentSearchViewState
        }

        fun getCurrentSearchViewState(): SearchViewState {
            return currentSearchViewState
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

        private const val DURATION_REVEAL_CLOSE = 300L
        private const val DURATION_REVEAL_OPEN = 400L
        private const val DURATION_LAYOUT_TRANSITION = 100L
        private const val DURATION_HOME_BUTTON = 300L

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