package io.pig.lkong.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import io.pig.lkong.http.cookie.impl.PersistentCookieStore

/**
 * @author yinhang
 * @since 2021/5/16
 */
object Prefs {

    private lateinit var preference: SharedPreferences
    private lateinit var cookiePreferences: SharedPreferences

    fun init(context: Context, name: String = "") {
        preference = if (name.isBlank()) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } else {
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
        cookiePreferences =
            context.getSharedPreferences(PersistentCookieStore.COOKIE_PREFS, Context.MODE_PRIVATE)
    }

    fun getCookiePreference(): SharedPreferences {
        return cookiePreferences
    }

    fun getLongPrefs(key: String, defaultValue: Long): LongPrefs {
        return LongPrefs(key, defaultValue, preference)
    }

    fun getStringPrefs(key: String, defaultValue: String): StringPrefs {
        return StringPrefs(key, defaultValue, preference)
    }

    fun getBoolPrefs(key: String, defaultValue: Boolean): BoolPrefs {
        return BoolPrefs(key, defaultValue, preference)
    }
}