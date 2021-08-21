package io.pig.lkong.theme

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import io.pig.lkong.util.ColorUtil
import io.pig.lkong.util.ThemeUtil

class ThemeConfig(val context: Context, val key: String) {

    private val editor = ThemeUtil.pref(context, key).edit()

    fun commit() {
        editor.putLong(VALUES_CHANGED, System.currentTimeMillis())
            .putBoolean(IS_CONFIGURED_KEY, true)
            .commit()
    }

    fun isConfigured(version: Int): Boolean {
        val prefs: SharedPreferences = ThemeUtil.pref(context, key)
        val lastVersion = prefs.getInt(IS_CONFIGURED_VERSION_KEY, -1)
        if (version > lastVersion) {
            prefs.edit().putInt(IS_CONFIGURED_VERSION_KEY, version).commit()
            return false
        }
        return true
    }

    fun activityTheme(@StyleRes theme: Int): ThemeConfig {
        val r = context.resources
        val name = r.getResourceName(theme)
        val defType = r.getResourceTypeName(theme)
        editor.putString(KEY_ACTIVITY_THEME, name)
        editor.putString(KEY_ACTIVITY_THEME_DEFAULT_TYPE, defType)
        return this
    }

    fun primaryColor(@ColorInt color: Int): ThemeConfig {
        editor.putInt(KEY_PRIMARY_COLOR, color)
        if (autoGeneratePrimaryDark(context, key)) {
            primaryColorDark(ColorUtil.darkenColor(color))
        }
        return this
    }

    fun primaryColorRes(@ColorRes colorRes: Int): ThemeConfig {
        return primaryColor(ContextCompat.getColor(context, colorRes))
    }

    fun coloredActionBar(applyToActionBar: Boolean): ThemeConfig {
        editor.putBoolean(KEY_APPLY_PRIMARY_SUPPORT_TAB, applyToActionBar)
        return this
    }

    fun accentColorRes(@ColorRes colorRes: Int): ThemeConfig {
        return accentColor(ContextCompat.getColor(context, colorRes))
    }

    fun accentColor(@ColorInt color: Int): ThemeConfig {
        editor.putInt(KEY_ACCENT_COLOR, color)
        return this
    }

    fun lightStatusBarMode(mode: Int): ThemeConfig {
        editor.putInt(KEY_LIGHT_STATUS_BAR_MODE, mode)
        return this
    }

    fun coloredStatusBar(color: Boolean): ThemeConfig {
        editor.putBoolean(KEY_APPLY_PRIMARY_DARK_STATUS_BAR, color)
        return this
    }

    fun lightToolbarMode(mode: Int): ThemeConfig {
        editor.putInt(KEY_LIGHT_TOOLBAR_MODE, mode)
        return this
    }

    fun coloredNavigationBar(applyToNavBar: Boolean): ThemeConfig {
        editor.putBoolean(KEY_APPLY_PRIMARY_NAVBAR, applyToNavBar)
        return this
    }

    fun textSizeSpForMode(spValue: Int, mode: String): ThemeConfig {
        val px =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spValue.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        return textSizePxForMode(px, mode)
    }

    // Text size
    private fun textSizePxForMode(pxValue: Int, mode: String): ThemeConfig {
        editor.putInt(mode, pxValue)
        return this
    }

    fun coloredNavigationBar(context: Context, key: String): Boolean {
        return ThemeUtil.pref(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_NAVBAR, false)
    }


    fun apply(activity: Activity) {
        commit()
    }

    private fun primaryColorDark(@ColorInt color: Int): ThemeConfig {
        editor.putInt(KEY_PRIMARY_COLOR_DARK, color)
        return this
    }

    private fun autoGeneratePrimaryDark(context: Context, key: String): Boolean {
        return ThemeUtil.pref(context, key).getBoolean(KEY_AUTO_GENERATE_PRIMARY_DARK, true)
    }

    companion object {
        private const val IS_CONFIGURED_KEY = "is_configured"
        private const val IS_CONFIGURED_VERSION_KEY = "is_configured_version"
        private const val VALUES_CHANGED = "values_changed"

        private const val KEY_ACCENT_COLOR = "accent_color"
        private const val KEY_PRIMARY_COLOR_DARK = "primary_color_dark"
        private const val KEY_LIGHT_STATUS_BAR_MODE = "light_status_bar_mode"
        private const val KEY_LIGHT_TOOLBAR_MODE = "light_toolbar_mode"
        private const val KEY_APPLY_PRIMARY_DARK_STATUS_BAR = "apply_primary_dark_status_bar"
        private const val KEY_APPLY_PRIMARY_NAVBAR = "apply_primary_navbar"
        private const val KEY_PRIMARY_COLOR = "primary_color"
        private const val KEY_AUTO_GENERATE_PRIMARY_DARK = "auto_generate_primary_dark"
        private const val KEY_ACTIVITY_THEME = "activity_theme"
        private const val KEY_ACTIVITY_THEME_DEFAULT_TYPE = "activity_theme_default_ype"
        private const val KEY_APPLY_PRIMARY_SUPPORT_TAB = "apply_primary_support_tab"
    }
}