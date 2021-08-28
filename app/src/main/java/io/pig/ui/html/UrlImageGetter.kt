package io.pig.ui.html

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.util.Log
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import io.pig.common.ui.R
import io.pig.lkong.util.ImageLoaderUtil.shouldDownloadImage
import java.io.IOException

class UrlImageGetter(var mContext: Context, downloadPolicy: Int) : ImageGetter {
    val mResources: Resources
    var emotionSize = 0
    var mMaxImageWidth = 0
    var mPlaceHolderResource = 0
    var mErrorResource = 0
    var mImageDownloadPolicy = 0
    fun setMaxImageWidth(maxImageWidth: Int): UrlImageGetter {
        mMaxImageWidth = maxImageWidth
        return this
    }

    fun setEmoticonSize(emoticonSize: Int): UrlImageGetter {
        emotionSize = emoticonSize
        return this
    }

    fun setPlaceHolder(@DrawableRes placeHolder: Int): UrlImageGetter {
        mPlaceHolderResource = placeHolder
        return this
    }

    fun setError(@DrawableRes error: Int): UrlImageGetter {
        mErrorResource = error
        return this
    }

    override fun getDrawable(source: String): Drawable {
        if (source.startsWith(EMOJI_PREFIX)) {
            val emojiFileName = source.substring(EMOJI_PREFIX.length)
            try {
                val emojiDrawable = Drawable.createFromStream(
                    mContext.assets.open(
                        "${EMOJI_PATH_WITH_SLASH}${emojiFileName}.png"
                    ), null
                )
                val right = if (emotionSize == 0) {
                    emojiDrawable.intrinsicWidth
                } else {
                    emotionSize
                }
                val left = if (emotionSize == 0) {
                    emojiDrawable.intrinsicHeight
                } else {
                    emotionSize
                }
                emojiDrawable.setBounds(0, 0, right, left)
                return emojiDrawable
            } catch (e: IOException) {
                Log.d("UrlImageGetter", "getDrawable() from assets failed.", e)
            }
        }
        val urlDrawable = UrlDrawable(mContext, mMaxImageWidth)
        if (shouldDownloadImage(mImageDownloadPolicy)) {
            Glide
                .with(mContext)
                .load(source)
                .placeholder(mPlaceHolderResource)
                .error(mErrorResource)
                .into(urlDrawable)
        } else {
            Glide
                .with(mContext)
                .load(mPlaceHolderResource)
                .placeholder(mPlaceHolderResource)
                .error(mErrorResource)
                .into(urlDrawable)
        }
        return urlDrawable
    }

    class UrlDrawable(private val context: Context, private val maxWidth: Int) : BitmapDrawable(
        context.resources, null as Bitmap?
    ), Target<Drawable>, Drawable.Callback {

        private var drawable: Drawable? = null

        private fun invalidateTargetTextView() {}

        fun setDrawableAndSelfBounds(newDrawable: Drawable?) {
            var drawableWidth = newDrawable!!.intrinsicWidth
            var drawableHeight = newDrawable.intrinsicHeight
            val newDrawableIntrinsicWidth = newDrawable.intrinsicWidth
            val newDrawableIntrinsicHeight = newDrawable.intrinsicHeight
            if (maxWidth != 0 && newDrawableIntrinsicWidth > maxWidth) {
                val ratio = newDrawableIntrinsicWidth.toDouble() / maxWidth.toDouble()
                drawableWidth = maxWidth
                drawableHeight = (newDrawableIntrinsicHeight.toDouble() / ratio).toInt()
            }
            newDrawable.setBounds(0, 0, drawableWidth, drawableHeight)
            drawable?.apply {
                callback = null
            }
            newDrawable.callback = this
            drawable = newDrawable
            this.setBounds(0, 0, drawableWidth, drawableHeight)
        }

        override fun draw(canvas: Canvas) {
            drawable?.apply {
                draw(canvas)
            }
        }

        override fun setAlpha(alpha: Int) {
            drawable?.apply {
                setAlpha(alpha)
            }
        }

        override fun setColorFilter(cf: ColorFilter?) {
            drawable?.apply {
                colorFilter = cf
            }
        }

        override fun getOpacity(): Int {
            return drawable?.opacity ?: PixelFormat.UNKNOWN
        }

        fun setDrawable(drawable: Drawable) {
            if (this.drawable != null) {
                this.drawable!!.callback = null
            }
            drawable.callback = this
            this.drawable = drawable
        }

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

        override fun getIntrinsicHeight(): Int {
            return drawable!!.intrinsicHeight
        }

        override fun getIntrinsicWidth(): Int {
            return drawable!!.intrinsicWidth
        }

        override fun onLoadStarted(placeholder: Drawable?) {
            setDrawableAndSelfBounds(placeholder)
            invalidateTargetTextView()
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            setDrawableAndSelfBounds(errorDrawable)
            invalidateTargetTextView()
        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            setDrawableAndSelfBounds(resource)
            invalidateTargetTextView()
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
    }

    companion object {
        private const val EMOJI_PREFIX = "http://img.lkong.cn/bq/"
        private const val EMOJI_PATH_WITH_SLASH = "emoji/"
    }

    /**
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     */
    init {
        mResources = mContext.resources
        mImageDownloadPolicy = downloadPolicy
        mErrorResource = R.drawable.placeholder_error
        mPlaceHolderResource = R.drawable.placeholder_loading
        // this.mEmoticonSize = UIUtils.sp2px(context, context.getResources().getDimension(R.dimen.text_size_body1));
    }
}