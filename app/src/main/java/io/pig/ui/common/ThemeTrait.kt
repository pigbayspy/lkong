package io.pig.ui.common

import android.app.Activity
import androidx.preference.PreferenceManager

/**
 * @author yinhang
 * @since 2021/7/6
 */

fun Activity.getThemeKey(): String {
    return if (PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("dark_theme", false)
    ) "dark_theme" else "light_theme"
}
