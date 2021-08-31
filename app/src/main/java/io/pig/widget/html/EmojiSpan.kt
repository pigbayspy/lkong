package io.pig.widget.html

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.DynamicDrawableSpan
import android.graphics.drawable.Drawable
import java.lang.Exception
import java.lang.ref.WeakReference

class EmojiSpan(
    private val mContext: Context,
    val source: String,
    private val mSize: Int,
    alignment: Int,
    private val textSize: Int
) : DynamicDrawableSpan(alignment) {

    private var mHeight: Int
    private var mWidth: Int
    private var mTop = 0
    private var mDrawable: Drawable? = null
    private var mDrawableRef: WeakReference<Drawable?>? = null

    init {
        mHeight = mSize
        mWidth = mHeight
    }

    override fun getDrawable(): Drawable {
        if (mDrawable == null) {
            try {
                if (source.startsWith(EMOJI_PREFIX)) {
                    val emojiFileName = source.substring(EMOJI_PREFIX.length)
                    val assetsPath = "${EMOJI_PATH_WITH_SLASH}${emojiFileName}.png"
                    mDrawable = getDrawableFromAssets(mContext, assetsPath)
                    mHeight = mSize
                    mWidth = mHeight * mDrawable!!.intrinsicWidth / mDrawable!!.intrinsicHeight
                    mTop = (textSize - mHeight) / 2
                    mDrawable!!.setBounds(0, mTop, mWidth, mTop + mHeight)
                }
            } catch (e: Exception) {
                // swallow
            }
        }
        return mDrawable!!
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val b = cachedDrawable
        canvas.save()
        var transY = bottom - b!!.bounds.bottom
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY = top + (bottom - top) / 2 - (b.bounds.bottom - b.bounds.top) / 2 - mTop
        }
        canvas.translate(x, transY.toFloat())
        b.draw(canvas)
        canvas.restore()
    }

    private val cachedDrawable: Drawable?
        get() {
            if (mDrawableRef == null || mDrawableRef!!.get() == null) {
                mDrawableRef = WeakReference(drawable)
            }
            return mDrawableRef!!.get()
        }

    companion object {
        private const val EMOJI_PREFIX = "https://avatar.lkong.com/bq"
        private const val EMOJI_PATH_WITH_SLASH = "emoji/"
        fun getDrawableFromAssets(context: Context, url: String): Drawable? {
            with(context.assets.open(url)) {
                return Drawable.createFromStream(this, null)
            }
        }
    }
}