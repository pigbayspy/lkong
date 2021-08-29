package io.pig.widget.html

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.lang.ref.WeakReference

class AsyncTargetDrawable : Drawable, Target<Drawable>, Drawable.Callback {
    private var mContext: WeakReference<Context?>
    private var mContainer: WeakReference<ImageSpanContainer?>
    private var mInnerDrawable: Drawable? = null
    private var mIdentityTag: Any
    private var mDrawableType: AsyncDrawableType
    private var mMaxWidth: Int
    private var mMaxHeight: Int

    constructor(
        mContext: Context?,
        mContainer: ImageSpanContainer?,
        mIdentityTag: Any,
        type: AsyncDrawableType,
        maxWidth: Int,
        maxHeight: Int
    ) {
        this.mContext = WeakReference(mContext)
        this.mContainer = WeakReference(mContainer)
        this.mIdentityTag = mIdentityTag
        mDrawableType = type
        mMaxWidth = maxWidth
        mMaxHeight = maxHeight
    }

    constructor(
        mContext: Context?,
        mContainer: ImageSpanContainer?,
        mIdentityTag: Any,
        type: AsyncDrawableType,
        placeHolderDrawable: Drawable?,
        maxWidth: Int,
        maxHeight: Int
    ) {
        this.mContext = WeakReference(mContext)
        this.mContainer = WeakReference(mContainer)
        this.mIdentityTag = mIdentityTag
        mDrawableType = type
        mMaxWidth = maxWidth
        mMaxHeight = maxHeight
        mInnerDrawable = placeHolderDrawable
        if (mInnerDrawable != null) {
            mInnerDrawable!!.bounds =
                getRecommendBound(mInnerDrawable, mMaxWidth, mMaxHeight, true)
            this.bounds =
                getRecommendBound(mInnerDrawable, mMaxWidth, mMaxHeight, true)
        }
    }

    fun setContainer(container: ImageSpanContainer?) {
        mContainer = WeakReference(container)
    }

    override fun draw(canvas: Canvas) {
        mInnerDrawable?.apply {
            draw(canvas)
        }
    }

    override fun setAlpha(alpha: Int) {
        mInnerDrawable?.apply {
            setAlpha(alpha)
        }
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mInnerDrawable?.apply {
            colorFilter = cf
        }
    }

    override fun getOpacity(): Int {
        return mInnerDrawable?.opacity ?: PixelFormat.UNKNOWN
    }

    fun setDrawable(drawable: Drawable?) {
        mInnerDrawable?.apply {
            callback = null
        }
        drawable!!.callback = this
        mInnerDrawable = drawable
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        setDrawable(placeholder)
        val bound = getRecommendBound(placeholder, mMaxWidth, mMaxHeight, true)
        placeholder!!.bounds = bound
        bounds = bound
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        setDrawable(errorDrawable)
        val bound = getRecommendBound(errorDrawable, mMaxWidth, mMaxHeight, true)
        errorDrawable!!.bounds = bound
        bounds = bound
        mContainer.get()?.apply {
            notifyImageSpanLoaded(mIdentityTag, errorDrawable, mDrawableType)
        }
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        checkNotNull(mContext.get()) { "Context is null, cannot create bitmap drawable." }
        val bound = Rect(0, 0, resource.intrinsicWidth, resource.intrinsicHeight)
        resource.bounds = bound
        mInnerDrawable?.apply {
            bounds = bound
        }
        setDrawable(resource)
        this.bounds = bound
        mContainer.get()?.apply {
            notifyImageSpanLoaded(mIdentityTag, resource, mDrawableType)
        }
    }

    override fun onLoadCleared(placeholder: Drawable?) {}
    override fun getSize(cb: SizeReadyCallback) {}
    override fun setRequest(request: Request?) {}
    override fun getRequest(): Request? {
        return null
    }

    override fun onStart() {}
    override fun onStop() {}
    override fun onDestroy() {}
    override fun invalidateDrawable(who: Drawable) {
        callback?.apply {
            invalidateDrawable(who)
        }
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, whenTime: Long) {
        callback?.apply {
            scheduleDrawable(who, what, whenTime)
        }
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        callback?.apply {
            unscheduleDrawable(who, what)
        }
    }

    override fun removeCallback(cb: SizeReadyCallback) {}

    companion object {
        const val TYPE_NORMAL_DRAWABLE = 11
        const val TYPE_EMOTICON_DRAWABLE = 12
        private fun getRecommendBound(
            drawable: Drawable?,
            maxWidth: Int,
            maxHeight: Int,
            forceUseMax: Boolean
        ): Rect {
            val result: Rect
            if (forceUseMax) {
                result = Rect(0, 0, maxWidth, maxHeight)
            } else {
                val width: Int
                val intrinsicWidth = drawable!!.intrinsicWidth
                width =
                    if (intrinsicWidth <= 0 || intrinsicWidth > maxWidth) maxWidth else intrinsicWidth
                val height: Int
                val intrinsicHeight = drawable.intrinsicHeight
                height =
                    if (intrinsicHeight <= 0 || intrinsicHeight > maxHeight) maxHeight else intrinsicHeight
                result = Rect(0, 0, width, height)
            }
            return result
        }
    }
}