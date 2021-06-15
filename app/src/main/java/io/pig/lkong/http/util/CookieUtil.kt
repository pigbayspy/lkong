package io.pig.lkong.http.util

import com.google.gson.JsonObject
import io.pig.lkong.util.HexUtil
import okhttp3.Cookie

/**
 * Cookie 工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object CookieUtil {

    private const val KEY_URL = "url"
    private const val KEY_NAME = "name"
    private const val KEY_VALUE = "value"
    private const val KEY_PERSISTENT = "persistent"
    private const val KEY_DOMAIN = "domain"
    private const val KEY_PATH = "path"
    private const val KEY_SECURE = "secure"
    private const val KEY_HTTP_ONLY = "httpOnly"
    private const val KEY_HOST_ONLY = "hostOnly"
    private const val NON_VALID_EXPIRES_AT: Long = -1L

    fun hasExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt < System.currentTimeMillis()
    }

    fun encode(url: String, cookie: Cookie): String {
        val json = JsonObject()
        json.addProperty(KEY_URL, url)
        json.addProperty(KEY_NAME, cookie.name)
        json.addProperty(KEY_VALUE, cookie.value)
        json.addProperty(
            KEY_PERSISTENT,
            if (cookie.persistent) cookie.expiresAt else NON_VALID_EXPIRES_AT
        )
        json.addProperty(KEY_DOMAIN, cookie.domain)
        json.addProperty(KEY_PATH, cookie.path)
        json.addProperty(KEY_SECURE, cookie.secure)
        json.addProperty(KEY_HTTP_ONLY, cookie.httpOnly)
        json.addProperty(KEY_HOST_ONLY, cookie.hostOnly)

        val jsonStr = json.toString()
        return HexUtil.bytesToHex(jsonStr.toByteArray())
    }
}