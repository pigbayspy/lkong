package io.pig.util.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * @author yinhang
 * @since 2021/7/1
 */
object Prefs {

    private lateinit var sharedPreference: SharedPreferences

    fun init(context: Context) {
        this.sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getString(key: String, defaultValue: String): String? {
        return sharedPreference.getString(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreference.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreference.getInt(key, defaultValue);
    }

    fun getStringPrefs(key: String, defaultValue: String): StringPref {
        return StringPref(key, defaultValue, sharedPreference)
    }
}