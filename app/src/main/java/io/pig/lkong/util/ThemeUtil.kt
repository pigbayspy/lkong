package io.pig.lkong.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.annotation.ColorInt
import io.pig.lkong.R
import io.pig.lkong.theme.ThemeConfig

/**
 * @author yinhang
 * @since 2021/6/13
 */
object ThemeUtil {

    private const val KEY_ACCENT_COLOR = "accent_color"
    private const val KEY_PRIMARY_COLOR = "primary_color"
    private const val KEY_TEXT_COLOR_PRIMARY = "text_color_primary"
    private const val KEY_TEXT_COLOR_SECONDARY = "text_color_secondary"
    private const val KEY_APPLY_PRIMARY_DARK_STATUS_BAR = "apply_primarydark_statusbar"
    private const val KEY_APPLY_PRIMARY_NAV_BAR = "apply_primary_navbar"

    private const val CONFIG_PREFS_KEY_DEFAULT = "[[theme-engine]]"
    private const val CONFIG_PREFS_KEY_CUSTOM = "[[theme-engine_%s]]"

    @ColorInt
    fun accentColor(context: Context): Int {
        return Color.parseColor("#263238")
    }

    @ColorInt
    fun textColorSecondary(context: Context): Int {
        // Todo
        return android.R.attr.textColorSecondary
    }

    @ColorInt
    fun accentColor(context: Context, key: String): Int {
        return prefs(context, key).getInt(
            KEY_ACCENT_COLOR,
            resolveColor(context, R.attr.colorAccent, Color.parseColor("#263238"))
        )
    }

    @ColorInt
    fun primaryColor(context: Context, key: String): Int {
        return prefs(context, key).getInt(
            KEY_PRIMARY_COLOR,
            resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64"))
        )
    }

    @ColorInt
    fun textColorPrimary(context: Context, key: String): Int {
        return prefs(context, key).getInt(
            KEY_TEXT_COLOR_PRIMARY,
            resolveColor(context, android.R.attr.textColorPrimary)
        );
    }

    @ColorInt
    fun textColorSecondary(context: Context, key: String): Int {
        return prefs(context, key).getInt(
            KEY_TEXT_COLOR_SECONDARY,
            resolveColor(context, android.R.attr.textColorSecondary)
        )
    }

    fun coloredStatusBar(context: Context, key: String): Boolean {
        return prefs(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_DARK_STATUS_BAR, true)
    }

    fun coloredNavigationBar(context: Context, key: String): Boolean {
        return prefs(context, key)
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

    private fun prefs(
        context: Context,
        key: String?
    ): SharedPreferences {
        return context.getSharedPreferences(
            if (key != null) String.format(
                CONFIG_PREFS_KEY_CUSTOM,
                key
            ) else CONFIG_PREFS_KEY_DEFAULT,
            Context.MODE_PRIVATE
        )
    }

    fun markChanged(context: Context, key: String) {
        ThemeConfig(context, key).commit()
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
}