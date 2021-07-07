package io.pig.lkong.theme

import android.content.Context
import io.pig.lkong.util.ThemeUtil

class ThemeConfig(val context: Context, val key: String) {

    private val editor = ThemeUtil.pref(context, key).edit()

    fun commit() {
        editor.putLong(VALUES_CHANGED, System.currentTimeMillis())
            .putBoolean(IS_CONFIGURED_KEY, true)
            .commit()
    }

    companion object {
        private const val IS_CONFIGURED_KEY = "is_configured"
        private const val VALUES_CHANGED = "values_changed"
    }
}