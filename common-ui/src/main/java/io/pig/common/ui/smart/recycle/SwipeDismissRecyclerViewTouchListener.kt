/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pig.common.ui.smart.recycle

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SwipeDismissRecyclerViewTouchListener(
    recyclerView: RecyclerView,
    callbacks: DismissCallback
) : OnTouchListener {
    // Cached ViewConfiguration and system-wide constant values
    private val mSlop: Int
    private val mMinFlingVelocity: Int
    private val mMaxFlingVelocity: Int
    private val mAnimationTime: Long

    // Fixed properties
    private val mRecyclerView: RecyclerView
    private val mCallbacks: DismissCallback
    private var mViewWidth = 1f // 1 and not 0 to prevent dividing by zero

    // Transient properties
    private val mPendingDismisses: MutableList<PendingDismissData> = ArrayList()
    private var mDismissAnimationRefCount = 0
    private var mDownX = 0f
    private var mDownY = 0f
    private var mSwiping = false
    private var mSwipingSlop = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mDownPosition = 0
    private var mDownView: View? = null
    private var mPaused = false

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     *
     * @param enabled Whether or not to watch for gestures.
     */
    fun setEnabled(enabled: Boolean) {
        mPaused = !enabled
    }

    /**
     * Returns an [android.widget.AbsListView.OnScrollListener] to be added to the [ ] using [android.widget.ListView.setOnScrollListener].
     * If a scroll listener is already assigned, the caller should still pass scroll changes through
     * to this listener. This will ensure that this [SwipeDismissRecyclerViewTouchListener] is
     * paused during list view scrolling.
     *
     * @see SwipeDismissRecyclerViewTouchListener
     */
    fun makeScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
        }
    }

    @SuppressLint("AndroidLintClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (mViewWidth < 2) {
            mViewWidth = mRecyclerView.width.toFloat()
        }
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mPaused) {
                    return false
                }

                // Find the child view that was touched (perform a hit test)
                val rect = Rect()
                val childCount = mRecyclerView.childCount
                val listViewCoords = IntArray(2)
                mRecyclerView.getLocationOnScreen(listViewCoords)
                val x = motionEvent.rawX.toInt() - listViewCoords[0]
                val y = motionEvent.rawY.toInt() - listViewCoords[1]
                var child: View
                var i = 0
                while (i < childCount) {
                    child = mRecyclerView.getChildAt(i)
                    child.getHitRect(rect)
                    if (rect.contains(x, y)) {
                        mDownView = child
                        break
                    }
                    i++
                }
                mDownView?.let {
                    mDownX = motionEvent.rawX
                    mDownY = motionEvent.rawY
                    mDownPosition = mRecyclerView.getChildLayoutPosition(it)
                    if (mCallbacks.canDismiss(mDownPosition)) {
                        val tracker = VelocityTracker.obtain()
                        mVelocityTracker = tracker
                        tracker.addMovement(motionEvent)
                    } else {
                        mDownView = null
                    }
                }
                return false
            }
            MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker?.let {
                    mDownView?.let { v ->
                        if (mSwiping) {
                            v.animate()
                                .translationX(0f).alpha(1f)
                                .setDuration(mAnimationTime)
                                .setListener(null)
                        }
                    }
                    it.recycle()
                    mVelocityTracker = null
                    mDownX = 0f
                    mDownY = 0f
                    mDownView = null
                    mDownPosition = INVALID_POSITION
                    mSwiping = false
                }
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.apply {
                    val deltaX = motionEvent.rawX - mDownX
                    addMovement(motionEvent)
                    computeCurrentVelocity(1000)
                    val velocityX = xVelocity
                    val absVelocityX = abs(velocityX)
                    val absVelocityY = abs(yVelocity)
                    var dismiss = false
                    var dismissRight = false
                    if (abs(deltaX) > mViewWidth / 2 && mSwiping) {
                        dismiss = true
                        dismissRight = deltaX > 0
                    } else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity && absVelocityY < absVelocityX && mSwiping) {
                        // dismiss only if flinging in the same direction as dragging
                        dismiss = velocityX < 0 == deltaX < 0
                        dismissRight = xVelocity > 0
                    }
                    if (dismiss && mDownPosition != INVALID_POSITION) {
                        // dismiss
                        val downView = mDownView // mDownView gets null before animation ends
                        val downPosition = mDownPosition
                        ++mDismissAnimationRefCount

                        val trans = if (dismissRight) {
                            mViewWidth
                        } else {
                            -mViewWidth
                        }

                        mDownView!!.animate()
                            .translationX(trans)
                            .alpha(0f)
                            .setDuration(mAnimationTime)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    performDismiss(downView!!, downPosition)
                                }
                            })
                    } else {
                        // cancel
                        mDownView!!.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(mAnimationTime)
                            .setListener(null)
                    }
                    recycle()
                    mDownX = 0f
                    mDownY = 0f
                    mDownView = null
                    mDownPosition = INVALID_POSITION
                    mSwiping = false
                }
                mVelocityTracker = null
            }
            MotionEvent.ACTION_MOVE -> {
                if (mVelocityTracker == null || mPaused) {
                    return false
                }
                mVelocityTracker!!.addMovement(motionEvent)
                val deltaX = motionEvent.rawX - mDownX
                val deltaY = motionEvent.rawY - mDownY
                if (abs(deltaX) > mSlop && abs(deltaY) < abs(deltaX) / 2) {
                    mSwiping = true
                    mSwipingSlop = if (deltaX > 0) mSlop else -mSlop
                    mRecyclerView.requestDisallowInterceptTouchEvent(true)

                    // Cancel ListView's touch (un-highlighting the item)
                    val cancelEvent = MotionEvent.obtain(motionEvent)
                    cancelEvent.action = MotionEvent.ACTION_CANCEL or
                            (motionEvent.action
                                    shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                    mRecyclerView.onTouchEvent(cancelEvent)
                    cancelEvent.recycle()
                }
                if (mSwiping) {
                    mDownView!!.translationX = deltaX - mSwipingSlop
                    mDownView!!.alpha = max(
                        0f, min(
                            1f,
                            1f - 2f * abs(deltaX) / mViewWidth
                        )
                    )
                    return true
                }
            }
        }
        return false
    }

    private fun performDismiss(dismissView: View, dismissPosition: Int) {
        // Animate the dismissed list item to zero-height and fire the dismiss callback when
        // all dismissed list item animations have completed. This triggers layout on each animation
        // frame; in the future we may want to do something smarter and more performant.
        val lp = dismissView.layoutParams
        val originalHeight = dismissView.height
        val animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                --mDismissAnimationRefCount
                if (mDismissAnimationRefCount == 0) {
                    // No active animations, process all pending dismisses.
                    // Sort by descending position
                    mPendingDismisses.sort()
                    val dismissPositions = IntArray(mPendingDismisses.size)
                    for (i in mPendingDismisses.indices.reversed()) {
                        dismissPositions[i] = mPendingDismisses[i].position
                    }
                    mCallbacks.onDismiss(mRecyclerView, dismissPositions)

                    // Reset mDownPosition to avoid MotionEvent.ACTION_UP trying to start a dismiss 
                    // animation with a stale position
                    mDownPosition = INVALID_POSITION
                    var lp: ViewGroup.LayoutParams
                    for (pendingDismiss in mPendingDismisses) {
                        // Reset view presentation
                        pendingDismiss.view.alpha = 1f
                        pendingDismiss.view.translationX = 0f
                        lp = pendingDismiss.view.layoutParams
                        lp.height = originalHeight
                        pendingDismiss.view.layoutParams = lp
                    }

                    // Send a cancel event
                    val time = SystemClock.uptimeMillis()
                    val cancelEvent = MotionEvent.obtain(
                        time, time,
                        MotionEvent.ACTION_CANCEL, 0f, 0f, 0
                    )
                    mRecyclerView.dispatchTouchEvent(cancelEvent)
                    mPendingDismisses.clear()
                }
            }
        })
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            lp.height = (valueAnimator.animatedValue as Int)
            dismissView.layoutParams = lp
        }
        mPendingDismisses.add(PendingDismissData(dismissPosition, dismissView))
        animator.start()
    }

    companion object {
        const val INVALID_POSITION = -1
    }

    /**
     * Constructs a new swipe-to-dismiss touch listener for the given list view.
     *
     * dismiss one or more list items.
     */
    init {
        val vc = ViewConfiguration.get(recyclerView.context)
        mSlop = vc.scaledTouchSlop
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity * 16
        mMaxFlingVelocity = vc.scaledMaximumFlingVelocity
        mAnimationTime = recyclerView.context.resources.getInteger(
            android.R.integer.config_shortAnimTime
        ).toLong()
        mRecyclerView = recyclerView
        mCallbacks = callbacks
    }
}