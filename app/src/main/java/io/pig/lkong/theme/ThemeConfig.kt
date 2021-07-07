package io.pig.lkong.theme

import android.app.Activity
import android.content.Context
import io.pig.lkong.util.ThemeUtil

class ThemeConfig(val context: Context, val key: String) {

    private val editor = ThemeUtil.pref(context, key).edit()

    fun commit() {
        editor.putLong(VALUES_CHANGED, System.currentTimeMillis())
            .putBoolean(IS_CONFIGURED_KEY, true)
            .commit()
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

    fun coloredNavigationBar(applyToNavBar:Boolean):ThemeConfig {
        editor.putBoolean(KEY_APPLY_PRIMARY_NAVBAR, applyToNavBar)
        return this
    }

    fun apply(activity: Activity) {
        commit()
    }

    companion object {
        private const val IS_CONFIGURED_KEY = "is_configured"
        private const val VALUES_CHANGED = "values_changed"

        private const val KEY_LIGHT_STATUS_BAR_MODE = "light_status_bar_mode"
        private const val KEY_LIGHT_TOOLBAR_MODE = "light_toolbar_mode"
        private const val KEY_APPLY_PRIMARY_DARK_STATUS_BAR = "apply_primary_dark_status_bar"
        private const val KEY_APPLY_PRIMARY_NAVBAR = "apply_primary_navbar"
    }
}