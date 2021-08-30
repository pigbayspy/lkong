package io.pig.widget.html

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import java.lang.ref.WeakReference

class ClickableImageSpan : DynamicDrawableSpanWithoutSpacing, PendingImageSpan {
    private var mDrawable: AsyncTargetDrawable
    private var mContext: WeakReference<Context>

    /**
     * Returns the source string that was saved during construction.
     */
    var source: String
        private set
    private var mSourceMiddle: String? = null
    private var mPlaceHolderRes: Int
    private var mErrorRes: Int
    private var mMaxWidth: Int
    private var mMaxHeight: Int
    private var mIdentityTag: Any
    private var mPicassoTag: Any
    private var mContainer: WeakReference<ImageSpanContainer>
    private var mIsLoaded = false

    /**
     * @param verticalAlignment one of [android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM] or
     * [android.text.style.DynamicDrawableSpan.ALIGN_BASELINE].
     */
    constructor(
        context: Context,
        container: ImageSpanContainer,
        identityTag: Any,
        picassoTag: Any,
        source: String,
        @DrawableRes placeholderRes: Int,
        @DrawableRes errorRes: Int,
        maxWidth: Int,
        maxHeight: Int,
        verticalAlignment: Int
    ) : super(verticalAlignment) {
        mContext = WeakReference(context)
        mContainer = WeakReference(container)
        mIdentityTag = identityTag
        mPicassoTag = picassoTag
        this.source = source
        mSourceMiddle =
            if (source.contains("sinaimg")) source.replace("/large/", "/bmiddle/") else source
        mPlaceHolderRes = placeholderRes
        mErrorRes = errorRes
        mMaxWidth = maxWidth
        mMaxHeight = maxHeight
        mDrawable = AsyncTargetDrawable(
            mContext.get(),
            mContainer.get(),
            mIdentityTag,
            AsyncDrawableType.NORMAL,
            mMaxWidth,
            mMaxHeight
        )
    }

    constructor(
        context: Context,
        container: ImageSpanContainer?,
        identityTag: Any,
        picassoTag: Any,
        source: String,
        @DrawableRes placeholderRes: Int,
        @DrawableRes errorRes: Int,
        maxWidth: Int,
        maxHeight: Int,
        verticalAlignment: Int,
        initDrawable: Drawable
    ) : super(verticalAlignment) {
        mContext = WeakReference(context)
        mContainer = WeakReference(container)
        mIdentityTag = identityTag
        mPicassoTag = picassoTag
        this.source = source
        mSourceMiddle =
            if (source.contains("sinaimg")) source.replace("/large/", "/bmiddle/") else source
        mPlaceHolderRes = placeholderRes
        mErrorRes = errorRes
        mMaxWidth = maxWidth
        mMaxHeight = maxHeight
        initDrawable.setBounds(0, 0, maxWidth, maxHeight)
        mDrawable = AsyncTargetDrawable(
            mContext.get(),
            mContainer.get(),
            mIdentityTag,
            AsyncDrawableType.NORMAL,
            initDrawable,
            mMaxWidth,
            mMaxHeight
        )
    }

    override val drawable: Drawable
        get() {
            return mDrawable
        }

    override fun loadImage(container: ImageSpanContainer) {
        mContainer = WeakReference(container)
        mDrawable.let {
            it.setContainer(container)
            val activity = mContext.get()
            if (!mIsLoaded && activity != null) {
                Glide
                    .with(activity)
                    .load(mSourceMiddle)
                    .error(mErrorRes)
                    .placeholder(mPlaceHolderRes)
                    .override(mMaxWidth, mMaxHeight)
                    .centerCrop()
                    .into(it)
                mIsLoaded = true
            }
        }
    }

    override fun loadImage(container: ImageSpanContainer, newMaxWidth: Int, backgroundColor: Int) {
        mMaxWidth = 256 //newMaxWidth;
        mContainer = WeakReference(container)
        mDrawable.let {
            it.setContainer(container)
            val activity = mContext.get()
            if (!mIsLoaded && activity != null) {
                if (activity is Activity) {
                    if (activity.isFinishing || activity.isFinishing) return
                }
                Glide
                    .with(activity)
                    .load(mSourceMiddle)
                    .error(mErrorRes)
                    .placeholder(mPlaceHolderRes)
                    .override(Int.MAX_VALUE, Int.MAX_VALUE)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .skipMemoryCache(true)
                    .transform(FitCenter())
                    .into(it)
                mIsLoaded = true
            }
        }
    }

    companion object {
        private const val MAX_HEIGHT = 1280
    }
}