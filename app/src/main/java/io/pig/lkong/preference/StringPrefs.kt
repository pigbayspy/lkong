package io.pig.lkong.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/5/16
 */
class StringPrefs(
    key: String, defaultValue: String, preferences: SharedPreferences
) : PrefsItem<String>(key, defaultValue, preferences) {

    override fun get(): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun set(value: String) {
        preferences.edit().putString(key, value).apply()
    }
}