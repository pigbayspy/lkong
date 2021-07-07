package io.pig.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import io.pig.lkong.R

/**
 * @author yinhang
 * @since 2021/7/7
 */
class ColorPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    private val themeKey:String

    init {
        layoutResource = R.layout.layout_theme_layout_custom
        widgetLayoutResource = R.layout.ate_preference_color
        isPersistent = false

        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ColorPreference, 0, 0)
        themeKey = try {
            a.getString(R.styleable.ColorPreference_theme_key_pref_color)!!
        } finally {
            a.recycle()
        }
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
    }
}