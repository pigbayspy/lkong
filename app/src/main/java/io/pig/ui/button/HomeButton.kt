package io.pig.ui.button

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.util.Property
import androidx.appcompat.widget.AppCompatImageView

class HomeButton(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    enum class IconState {
        BURGER, ARROW;

        fun toDrawablePosition(): Int {
            return if (this == ARROW) {
                1
            } else 0
        }
    }

    private val arrowPositionProperty = ArrowDrawablePositionProperty()
    private val arrowDrawable: SupportDrawerArrowDrawable = SupportDrawerArrowDrawable(context)
    private var buttonState = IconState.BURGER
    private var animationDuration = 300L

    fun setArrowDrawableColor(color: Int) {
        val colorFilter: ColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        arrowDrawable.colorFilter = colorFilter
    }

    init {
        val colorFilter: ColorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        arrowDrawable.colorFilter = colorFilter
        setImageDrawable(arrowDrawable)
    }

    fun setState(state: IconState) {
        buttonState = state
        arrowDrawable.setPosition(buttonState.toDrawablePosition().toFloat())
    }

    fun animateState(state: IconState) {
        val to = state.toDrawablePosition().toFloat()
        val from = buttonState.toDrawablePosition().toFloat()
        buttonState = state
        if (from.compareTo(to) != 0) {
            ObjectAnimator.ofFloat(arrowDrawable, arrowPositionProperty, from, to)
                .setDuration(animationDuration).start()
        }
    }

    fun setAnimationDuration(animationDuration: Long) {
        this.animationDuration = animationDuration
    }

    internal class ArrowDrawablePositionProperty :
        Property<SupportDrawerArrowDrawable, Float>(java.lang.Float.TYPE, "position") {

        override fun set(obj: SupportDrawerArrowDrawable, value: Float) {
            obj.setPosition(value)
        }

        override fun get(obj: SupportDrawerArrowDrawable): Float {
            return obj.getPosition()
        }
    }
}