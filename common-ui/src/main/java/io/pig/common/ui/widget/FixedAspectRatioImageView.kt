package io.pig.common.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import io.pig.common.ui.R

class FixedAspectRatioImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs) {

    private val mFixedOrientation: Int
    private val mWidthWeight: Int
    private val mHeightWeight: Int
    private val mAspectRatio: Float

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioAttr, 0, 0)
        mFixedOrientation = a.getInt(R.styleable.FixedAspectRatioAttr_far_fixed_orientation, 0)
        mWidthWeight = a.getInt(R.styleable.FixedAspectRatioAttr_far_width_weight, 4)
        mHeightWeight = a.getInt(R.styleable.FixedAspectRatioAttr_far_height_weight, 3)
        mAspectRatio = mWidthWeight.toFloat() / mHeightWeight
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        try {
            var width = measuredWidth
            var height = measuredHeight
            if (drawable == null) {
                setMeasuredDimension(0, 0)
            } else {
                if (mFixedOrientation == HORIZONTAL) {
                    // Image is wider than the display (ratio)
                    height = (width / mAspectRatio).toInt()
                } else {
                    // Image is taller than the display (ratio)
                    width = (height * mAspectRatio).toInt()
                }
                setMeasuredDimension(width, height)
            }
        } catch (e: Exception) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    companion object {
        private const val HORIZONTAL = 1
    }
}