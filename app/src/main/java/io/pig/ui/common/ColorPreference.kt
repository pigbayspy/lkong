package io.pig.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import io.pig.lkong.R
import io.pig.ui.BorderCircleView

/**
 * @author yinhang
 * @since 2021/7/7
 */
class ColorPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private val themeKey: String
    private var border: Int = 0
    private var color = 0
    private var view: View? = null

    init {
        layoutResource = R.layout.layout_theme_layout_custom
        widgetLayoutResource = R.layout.layout_setting_preference_color
        isPersistent = false

        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPreference, 0, 0)
        themeKey = try {
            a.getString(R.styleable.ColorPreference_theme_key_pref_color)!!
        } finally {
            a.recycle()
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        this.view = holder.itemView
        invalidateColor()
    }

    fun setColor(color: Int, border: Int) {
        this.color = color
        this.border = border
        invalidateColor()
    }

    private fun invalidateColor() {
        view?.apply {
            val circleView:View = findViewById(R.id.setting_preference_color_circle)
            if (circleView is BorderCircleView) {
                if (color != 0) {
                    circleView.setVisibility(View.VISIBLE)
                    circleView.setBackgroundColor(color)
                    circleView.setBorderColor(border)
                } else {
                    circleView.setVisibility(View.GONE)
                }
            }
        }
    }
}