package io.pig.lkong.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.annotation.ColorInt
import io.pig.lkong.R

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
    fun textColorSecondary(context: Context,key: String): Int {
        return prefs(context, key).getInt(
            KEY_TEXT_COLOR_SECONDARY,
            resolveColor(context, android.R.attr.textColorSecondary)
        )
    }

    fun coloredStatusBar( context: Context,key: String): Boolean {
        return prefs(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_DARK_STATUS_BAR, true)
    }

    fun coloredNavigationBar( context: Context, key: String): Boolean {
        return prefs(context, key)
            .getBoolean(KEY_APPLY_PRIMARY_NAV_BAR, false)
    }

    fun textSizeForMode(
        context: Context,
        key: String,
        mode: String
    ): Int {
        var size: Int = prefs(context, key).getInt(mode, 0)
        if (size == 0) {
            size = when (mode) {
                com.afollestad.appthemeengine.Config.TEXTSIZE_CAPTION -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_caption
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_BODY -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_body
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_SUBHEADING -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_subheading
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_TITLE -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_title
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_HEADLINE -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_headline
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_DISPLAY1 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display1
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_DISPLAY2 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display2
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_DISPLAY3 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display3
                )
                com.afollestad.appthemeengine.Config.TEXTSIZE_DISPLAY4 -> context.resources.getDimensionPixelSize(
                    R.dimen.ate_default_textsize_display4
                )
                else -> throw IllegalArgumentException(
                    String.format(
                        "Unknown text size mode: %s",
                        mode
                    )
                )
            }
        }
        return size
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