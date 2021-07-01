package io.pig.util.preference

import android.content.SharedPreferences

/**
 * @author yinhang
 * @since 2021/7/1
 */
class StringPref(
    key: String, default: String,
    preference: SharedPreferences
) : PrefItem<String>(key, default, preference) {

    override fun get(): String {
        return preference.getString(key, default) ?: default
    }

    override fun set(value: String) {
        preference.edit().putString(key, value).apply()
    }
}