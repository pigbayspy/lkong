package io.pig.ui.common

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.appbar.AppBarLayout
import io.pig.lkong.util.ThemeUtil

/**
 * @author yinhang
 * @since 2021/7/6
 */

fun Activity.getThemeKey(): String {
    return if (PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("dark_theme", false)
    ) "dark_theme" else "light_theme"
}

fun Activity.getPrimaryColor(): Int {
    return ThemeUtil.primaryColor(this, getThemeKey())
}

fun Activity.processToolbar(toolbar: Toolbar) {
    val key = getThemeKey()
    val toolbarColor = ThemeUtil.toolbarColor(this, key)
    val tintColor = ThemeUtil.getToolbarTitleColor(this, key, toolbarColor)
    val root = toolbar.parent
    if (root is AppBarLayout) {
        root.setBackgroundColor(toolbarColor)
        toolbar.setTitleTextColor(tintColor)
    }
    toolbar.setSubtitleTextColor(ThemeUtil.getToolbarSubtitleColor(this, key, toolbarColor))
}

fun Fragment.getThemeKey(): String {
    return if (PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getBoolean("dark_theme", false)
    ) "dark_theme" else "light_theme"
}

fun Fragment.getPrimaryColor(): Int {
    return ThemeUtil.primaryColor(requireContext(), getThemeKey())
}
