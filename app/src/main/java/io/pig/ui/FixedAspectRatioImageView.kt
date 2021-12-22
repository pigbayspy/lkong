package io.pig.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import io.pig.lkong.R

class FixedAspectRatioImageView(context: Context, attrs: AttributeSet, defStyle: Int) :
    AppCompatImageView(
        context, attrs, defStyle
    ) {
    private var mFixedOrientation = 0
    private var mWidthWeight = 0
    private var mHeightWeight = 0
    private var mAspectRatio = 0.0f

    init {
        val attributes =
            context.theme.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioAttr, 0, 0)
        mFixedOrientation =
            attributes.getInt(R.styleable.FixedAspectRatioAttr_far_fixed_orientation, 0)
        mWidthWeight = attributes.getInt(R.styleable.FixedAspectRatioAttr_far_width_weight, 4)
        mHeightWeight = attributes.getInt(R.styleable.FixedAspectRatioAttr_far_height_weight, 3)
        mAspectRatio = mWidthWeight.toFloat() / mHeightWeight
        attributes.recycle()
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
                    height = (width / mAspectRatio).toInt()
                } else {
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