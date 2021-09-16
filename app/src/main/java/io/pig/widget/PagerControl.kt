package io.pig.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import io.pig.lkong.R
import io.pig.widget.listener.OnPagerControlListener

class PagerControl : FrameLayout {

    private var isVisible = true

    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private var onPagerControlListener: OnPagerControlListener? = null

    private lateinit var pageIndicatorButton: Button
    private lateinit var prevPageButton: ImageButton
    private lateinit var nextPageButton: ImageButton

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        prevPageButton = findViewById(R.id.pager_control_button_backward)
        pageIndicatorButton = findViewById(R.id.pager_control_button_page_indicator)
        nextPageButton = findViewById(R.id.pager_control_button_forward)
        pageIndicatorButton.setOnClickListener {
            onPagerControlListener?.onPageIndicatorClick()
        }
        prevPageButton.setOnClickListener {
            onPagerControlListener?.onBackwardClick()
        }
        nextPageButton.setOnClickListener {
            onPagerControlListener?.onForwardClick()
        }
    }

    fun getBackwardButton(): ImageButton {
        return prevPageButton
    }

    fun getForwardButton(): ImageButton {
        return nextPageButton
    }

    fun getContainer(): LinearLayout {
        return findViewById(R.id.pager_control_container)!!
    }

    fun setOnPagerControlListener(onPagerControlListener: OnPagerControlListener?) {
        this.onPagerControlListener = onPagerControlListener
    }

    fun setPageIndicatorText(charSequence: CharSequence?) {
        this.pageIndicatorButton.text = charSequence
    }

    fun show(animate: Boolean = true) {
        toggle(true, animate, false)
    }

    fun hide(animate: Boolean = true) {
        toggle(false, animate, false)
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (isVisible != visible || force) {
            isVisible = visible
            val height = height
            if (height == 0 && !force) {
                val vto = viewTreeObserver
                if (vto.isAlive) {
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = viewTreeObserver
                            if (currentVto.isAlive) {
                                currentVto.removeOnPreDrawListener(this)
                            }
                            toggle(visible, animate, true)
                            return true
                        }
                    })
                    return
                }
            }
            val translationY = if (visible) 0 else {
                height + getMarginBottom()
            }
            if (animate) {
                animate().setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                    .translationY(translationY.toFloat())
            }
        }
    }

    private fun getMarginBottom(): Int {
        var marginBottom = 0
        val layoutParams = layoutParams
        if (layoutParams is MarginLayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return marginBottom
    }

    companion object {
        private const val TRANSLATE_DURATION_MILLIS = 200
    }
}