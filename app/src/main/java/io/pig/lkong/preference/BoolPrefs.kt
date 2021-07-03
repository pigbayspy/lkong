package io.pig.lkong.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/7/3
 */
class BoolPrefs(
    key: String, defaultValue: Boolean, preferences: SharedPreferences
) : PrefsItem<Boolean>(key, defaultValue, preferences) {

    override fun get(): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    override fun set(value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }
}