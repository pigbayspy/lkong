package io.pig.ui.button

import android.content.Context
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable

class SupportDrawerArrowDrawable(themedContext: Context) : DrawerArrowDrawable(themedContext) {
    fun setPosition(position: Float) {
        if (position == 1f) {
            setVerticalMirror(true)
        } else if (position == 0f) {
            setVerticalMirror(false)
        }
        super.setProgress(position)
    }

    fun getPosition(): Float {
        return super.getProgress()
    }
}