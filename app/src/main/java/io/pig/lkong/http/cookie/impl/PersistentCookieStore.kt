package io.pig.lkong.http.cookie.impl

import android.content.SharedPreferences
import android.text.TextUtils
import io.pig.lkong.http.cookie.CookieStore
import io.pig.lkong.http.util.CookieUtil
import io.pig.lkong.preference.Prefs
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PersistentCookieStore : CookieStore {

    companion object {
        const val COOKIE_PREFS = "CookiePrefsFile"
        private const val HOST_NAME_PREFIX = "host_"
        private const val COOKIE_NAME_PREFIX = "cookie_"
    }

    private val cookies: MutableMap<String, ConcurrentHashMap<String, Cookie>>
    private val cookiePrefs: SharedPreferences

    /** Construct a persistent cookie store.   */
    init {
        cookiePrefs = Prefs.getCookiePreference()
        cookies = mutableMapOf()
        val tempCookieMap = HashMap<String, Any>(cookiePrefs.all)
        for ((key, tempCookieSet) in tempCookieMap) {
            if (!key.contains(HOST_NAME_PREFIX)) {
                continue
            }
            val cookieNames = tempCookieSet as String?
            if (cookieNames.isNullOrBlank()) {
                continue
            }
            val cookieSet = cookies.run {
                val t = get(key)
                if (t != null) {
                    return@run t
                } else {
                    val c = ConcurrentHashMap<String, Cookie>()
                    put(key, c)
                    return@run c
                }
            }
            val cookieNameArr = cookieNames.split(",").toTypedArray()
            for (name in cookieNameArr) {
                val encodedCookie = cookiePrefs.getString("cookie_$name", null) ?: continue
                val decodedCookie = CookieUtil.decode(encodedCookie)
                cookieSet[name] = decodedCookie
            }
        }
        tempCookieMap.clear()
        clearExpired()
    }

    /** 移除失效cookie  */
    private fun clearExpired() {
        val prefsWriter = cookiePrefs.edit()
        for ((url, cookieMap) in cookies) {
            var changeFlag = false
            for ((name, cookie) in cookieMap) {
                if (CookieUtil.hasExpired(cookie)) {
                    // Clear cookies from local store
                    cookieMap.remove(name)
                    // Clear cookies from persistent store
                    prefsWriter.remove(COOKIE_NAME_PREFIX + name)
                    // We've cleared at least one
                    changeFlag = true
                }
            }
            // Update names in persistent store
            if (changeFlag) {
                prefsWriter.putString(url, TextUtils.join(",", cookies.keys))
            }
        }
        prefsWriter.apply()
    }

    override fun add(httpUrl: HttpUrl, cookie: Cookie) {
        if (!cookie.persistent) {
            return
        }
        val name = cookieName(cookie)
        val hostKey = hostName(httpUrl)

        // Save cookie into local store, or remove if expired
        val hostKeyCookies = cookies.run {
            val t = get(hostKey)
            if (t != null) {
                return@run t
            } else {
                val c = ConcurrentHashMap<String, Cookie>()
                put(hostKey, c)
                return@run c
            }
        }
        hostKeyCookies[name] = cookie
        // Save cookie into persistent store
        val prefsWriter = cookiePrefs.edit()
        // 保存httpUrl对应的所有cookie的name
        prefsWriter.putString(hostKey, TextUtils.join(",", hostKeyCookies.keys))
        // 保存cookie
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, CookieUtil.encode(cookie))
        prefsWriter.apply()
    }

    override fun add(httpUrl: HttpUrl, cookies: List<Cookie>) {
        for (cookie in cookies) {
            if (CookieUtil.hasExpired(cookie)) {
                continue
            }
            this.add(httpUrl, cookie)
        }
    }

    override fun get(httpUrl: HttpUrl): List<Cookie> {
        return this[hostName(httpUrl)]
    }

    override fun getCookies(): List<Cookie> {
        val result = ArrayList<Cookie>()
        for (hostKey in cookies.keys) {
            result.addAll(this[hostKey])
        }
        return result
    }

    /** 获取cookie集合  */
    private operator fun get(hostKey: String): List<Cookie> {
        val result = ArrayList<Cookie>()
        cookies[hostKey]?.apply {
            for (cookie in values) {
                if (CookieUtil.hasExpired(cookie)) {
                    this.remove(hostKey, cookie)
                } else {
                    result.add(cookie)
                }
            }
        }
        return result
    }

    override fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean {
        return this.remove(hostName(httpUrl), cookie)
    }

    /** 从缓存中移除cookie  */
    private fun remove(hostKey: String, cookie: Cookie): Boolean {
        val name = cookieName(cookie)
        val cookieSet = cookies[hostKey]
        cookieSet?.apply {
            if (contains(name)) {
                remove(name)
                val prefsWriter = cookiePrefs.edit()

                // 从本地缓存中移出对应cookie
                prefsWriter.remove(COOKIE_NAME_PREFIX + name)

                // 保存 httpUrl 对应的所有 cookie 的 name
                prefsWriter.putString(hostKey, TextUtils.join(",", keys))
                prefsWriter.apply()
                return true
            }
        }
        return false
    }

    override fun removeAll() {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
    }

    private fun hostName(httpUrl: HttpUrl): String {
        return if (httpUrl.host.startsWith(HOST_NAME_PREFIX)) {
            httpUrl.host
        } else {
            HOST_NAME_PREFIX + httpUrl.host
        }
    }

    private fun cookieName(cookie: Cookie): String {
        return cookie.name + cookie.domain
    }
}