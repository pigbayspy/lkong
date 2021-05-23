package io.pig.lkong.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/5/23
 */
class IntPrefs(
    key: String, defaultValue: Int, preferences: SharedPreferences
) : PrefsItem<Int>(key, defaultValue, preferences) {

    override fun get(): Int {
        return preferences.getInt(key, defaultValue)
    }

    override fun set(value: Int) {
        preferences.edit().putInt(key, value).apply()
    }
}