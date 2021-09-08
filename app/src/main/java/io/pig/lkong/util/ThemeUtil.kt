package io.pig.lkong.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.PreferenceManager
import io.pig.lkong.R
import io.pig.lkong.theme.ThemeConfig

/**
 * @author yinhang
 * @since 2021/6/13
 */
object ThemeUtil {

    const val KEY_ACCENT_COLOR = "accent_color"
    const val KEY_PRIMARY_COLOR = "primary_color"
    const val KEY_TOOLBAR_COLOR = "toolbar_color"
    const val KEY_LIGHT_TOOLBAR_MODE = "light_toolbar_mode"
    const val KEY_TEXT_COLOR_PRIMARY = "text_color_primary"
    const val KEY_TEXT_COLOR_SECONDARY = "text_color_secondary"
    const val KEY_LIGHT_STATUS_BAR_MODE = "light_status_bar_mode"
    const val KEY_PRIMARY_COLOR_DARK = "primary_color_dark"
    const val KEY_APPLY_PRIMARY_DARK_STATUS_BAR = "apply_primary_dark_status_bar"
    const val KEY_APPLY_PRIMARY_NAV_BAR = "apply_primary_navbar"
    const val KEY_TEXT_COLOR_PRIMARY_INVERSE = "text_color_primary_inverse"

    const val DARK_THEME = "dark_theme"
    const val LIGHT_THEME = "light_theme"

    private const val CONFIG_PREFS_KEY_DEFAULT = "[[theme-engine]]"
    private const val CONFIG_PREFS_KEY_CUSTOM = "[[theme-engine_%s]]"

    const val LIGHT_TOOLBAR_AUTO = 1
    const val LIGHT_TOOLBAR_ON = 2
    const val LIGHT_TOOLBAR_OFF = 3

    @ColorInt
    fun textColorSecondary(context: Context): Int {
        val key = if (PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(DARK_THEME, false)
        ) {
            DARK_THEME
        } else {
            LIGHT_THEME
        }
        return textColorSecondary(context, key)
    }

    @ColorInt
    fun accentColor(context: Context, key: String): Int {
        return pref(context, key).getInt(
            KEY_ACCENT_COLOR,
            resolveColor(context, R.attr.colorAccent, Color.parseColor("#263238"))
        )
    }

    @ColorInt
    fun accentColor(context: Context): Int {
        val key = if (PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(DARK_THEME, false)
        ) {
            DARK_THEME
        } else {
            LIGHT_THEME
        }
        return pref(context, key).getInt(
            KEY_ACCENT_COLOR,
            resolveColor(context, R.attr.colorAccent, Color.parseColor("#263238"))
        )
    }

    @ColorInt
    fun primaryColor(context: Context, key: String): Int {
        return pref(context, key).getInt(
            KEY_PRIMARY_COLOR,
            resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64"))
        )
    }

    @ColorInt
    fun textColorPrimary(context: Context, key: String): Int {
        return pref(context, key).getInt(
            KEY_TEXT_COLOR_PRIMARY,
            resolveColor(context, android.R.attr.textColorPrimary)
        )
    }

    @ColorInt
    fun textColorSecondary(context: Context, key: String): Int {
        return pref(context, key).getInt(
            KEY_TEXT_COLOR_SECONDARY,
            resolveColor(context, android.R.attr.textColorSecondary)
        )
    }

    @ColorInt
    fun toolbarColor(context: Context, key: String): Int {
        return pref(context, key).getInt(KEY_TOOLBAR_COLOR, primaryColor(context, key))
    }

    @ColorInt
    fun getToolbarTitleColor(
        context: Context,
        key: String,
        toolbarColor: Int
    ): Int {
        val color = if (isLightToolbar(context, key, toolbarColor)) {
            R.color.theme_primary_text_light
        } else {
            R.color.theme_primary_text_dark
        }
        return ContextCompat.getColor(context, color)
    }

    @ColorInt
    fun getToolbarSubtitleColor(context: Context, key: String, toolbarColor: Int): Int {
        val color = if (isLightToolbar(context, key, toolbarColor)) {
            R.color.theme_secondary_text_light
        } else {
            R.color.theme_secondary_text_dark
        }
        return ContextCompat.getColor(context, color)
    }

    @ColorInt
    fun makeColorDarken(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= factor // value component
        return Color.HSVToColor(hsv)
    }

    private fun isLightToolbar(
        context: Context,
        key: String,
        toolbarColor: Int
    ): Boolean {
        return when (lightToolbarMode(context, key)) {
            LIGHT_TOOLBAR_ON -> true
            LIGHT_TOOLBAR_OFF -> false
            LIGHT_TOOLBAR_AUTO -> isColorLight(
                toolbarColor
            )
            else -> isColorLight(toolbarColor)
        }
    }

    fun isColorLight(color: Int): Boolean {
        if (color == Color.BLACK) {
            return false
        } else if (color == Color.WHITE || color == Color.TRANSPARENT) {
            return true
        }
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.4
    }

    private fun lightToolbarMode(context: Context, key: String): Int {
        var value: Int = pref(context, key)
            .getInt(KEY_LIGHT_TOOLBAR_MODE, LIGHT_TOOLBAR_AUTO)
        if (value < 1) {
            value = LIGHT_TOOLBAR_AUTO
        }
        return value
    }

    fun coloredStatusBar(context: Context, key: String): Boolean {
        return pref(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_DARK_STATUS_BAR, true)
    }

    fun coloredNavigationBar(context: Context, key: String): Boolean {
        return pref(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_NAV_BAR, false)
    }

    fun pref(context: Context, key: String): SharedPreferences {
        return context.getSharedPreferences(
            String.format(
                CONFIG_PREFS_KEY_CUSTOM,
                key
            ), Context.MODE_PRIVATE
        )
    }

    fun markChanged(context: Context, key: String) {
        ThemeConfig(context, key).commit()
    }

    fun markChanged(context: Context, vararg keys: String) {
        for (key in keys) {
            ThemeConfig(context, key).commit()
        }
    }

    private fun resolveColor(context: Context, attr: Int): Int {
        return resolveColor(context, attr, 0)
    }

    private fun resolveColor(context: Context, attr: Int, fallback: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        return try {
            a.getColor(0, fallback)
        } finally {
            a.recycle()
        }
    }

    fun setTint(drawable: Drawable, color: Int) {
        DrawableCompat.setTint(drawable, color)
    }

    @ColorInt
    fun textColorPrimaryInverse(context: Context, key: String): Int {
        return pref(context, key).getInt(
            KEY_TEXT_COLOR_PRIMARY_INVERSE,
            resolveColor(context, android.R.attr.textColorPrimaryInverse)
        )
    }
}