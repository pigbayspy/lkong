package io.pig.widget.util

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

class QuickReturnHelper(private val targetView: View, private val direction: Int) {

    private var isVisible = true

    private val interpolator: Interpolator = AccelerateDecelerateInterpolator()

    fun show(animate: Boolean = true) {
        toggle(true, animate, false)
    }

    fun hide(animate: Boolean = true) {
        toggle(false, animate, false)
    }

    fun isVisible(): Boolean {
        return isVisible
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (isVisible != visible || force) {
            isVisible = visible
            val height = targetView.height
            if (height == 0 && !force) {
                val vto = targetView.viewTreeObserver
                if (vto.isAlive) {
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = targetView.viewTreeObserver
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
            val translationYWithTarget: Int = if (direction == ANIMATE_DIRECTION_DOWN) {
                height + getMarginTop()
            } else if (direction == ANIMATE_DIRECTION_UP) {
                -(height + getMarginTop())
            } else {
                throw IllegalArgumentException("Unknown direction.")
            }
            val translationY = if (visible) 0 else translationYWithTarget
            if (animate) {
                targetView.animate().setInterpolator(interpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                    .translationY(translationY.toFloat())
            } else {
                targetView.translationY = translationY.toFloat()
            }
        }
    }

    private fun getMarginBottom(): Int {
        var marginBottom = 0
        val layoutParams: ViewGroup.LayoutParams = targetView.layoutParams
        if (layoutParams is MarginLayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return marginBottom
    }

    private fun getMarginTop(): Int {
        var marginTop = 0
        val layoutParams: ViewGroup.LayoutParams = targetView.layoutParams
        if (layoutParams is MarginLayoutParams) {
            marginTop = layoutParams.topMargin
        }
        return marginTop
    }

    companion object {
        const val ANIMATE_DIRECTION_UP = 12
        const val ANIMATE_DIRECTION_DOWN = 13
        private const val TRANSLATE_DURATION_MILLIS = 200
    }
}