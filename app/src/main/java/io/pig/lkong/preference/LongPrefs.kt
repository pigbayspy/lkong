package io.pig.lkong.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/5/16
 */
class LongPrefs(
    key: String, defaultValue: Long, preferences: SharedPreferences
) : PrefsItem<Long>(key, defaultValue, preferences) {

    override fun get(): Long {
        return preferences.getLong(key, defaultValue)
    }

    override fun set(value: Long) {
        preferences.edit().putLong(key, value).apply()
    }
}