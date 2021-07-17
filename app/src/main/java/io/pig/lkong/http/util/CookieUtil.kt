package io.pig.lkong.http.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.pig.lkong.util.HexUtil
import okhttp3.Cookie
import java.nio.charset.StandardCharsets

/**
 * Cookie 工具类
 *
 * @author yinhang
 * @since 2021/05/18
 */
object CookieUtil {

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

    fun encode(cookie: Cookie): String {
        val json = JsonObject()
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

    fun decode(encodedCookie: String): Cookie {
        val bytes = HexUtil.hexToBytes(encodedCookie)
        val jsonStr = String(bytes, StandardCharsets.UTF_8)
        val obj = JsonParser.parseString(jsonStr) as JsonObject
        val builder = Cookie.Builder()
        builder.name(obj.get(KEY_NAME).asString)
        builder.value(obj.get(KEY_VALUE).asString)
        val expiresAt: Long = obj.get(KEY_PERSISTENT).asLong
        if (expiresAt != NON_VALID_EXPIRES_AT) {
            builder.expiresAt(expiresAt)
        }
        val domain: String = obj.get(KEY_DOMAIN).asString
        builder.domain(domain)
        builder.path(obj.get(KEY_PATH).asString)
        if (obj.get(KEY_HTTP_ONLY).asBoolean) {
            builder.httpOnly()
        }
        if (obj.get(KEY_SECURE).asBoolean) {
            builder.secure()
        }
        if (obj.get(KEY_HOST_ONLY).asBoolean) {
            builder.hostOnlyDomain(domain)
        }
        return builder.build()
    }
}