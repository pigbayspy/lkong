package io.pig.lkong.http.cookie

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.pig.lkong.util.HexUtil
import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.nio.charset.StandardCharsets

/**
 * @author yinhang
 * @since 2021/7/2
 */
object SerializableCookie {

    private const val KEY_URL = "url"
    private const val KEY_NAME = "name"
    private const val KEY_VALUE = "value"
    private const val KEY_PERSISTENT = "persistent"
    private const val KEY_DOMAIN = "domain"
    private const val KEY_PATH = "path"
    private const val KEY_HTTP_ONLY = "httpOnly"
    private const val KEY_SECURE = "secure"
    private const val KEY_HOST_ONLY = "hostOnly"
    private const val NON_VALID_EXPIRES_AT = -1L

    fun decode(encodedCookie: String): Pair<HttpUrl, Cookie> {
        val bytes = HexUtil.hexToBytes(encodedCookie)
        val jsonStr = String(bytes, StandardCharsets.UTF_8)
        val obj = JsonParser.parseString(jsonStr) as JsonObject
        val url = obj[KEY_URL].asString
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
        val cookie = builder.build()
        return (url.toHttpUrl() to cookie)
    }

    fun encode(url: String, cookie: Cookie): String {
        val obj = JsonObject()
        obj.addProperty(KEY_URL, url);
        obj.addProperty(KEY_NAME, cookie.name);
        obj.addProperty(KEY_VALUE, cookie.value);
        val persistent = if (cookie.persistent) {
            cookie.expiresAt
        } else {
            NON_VALID_EXPIRES_AT
        }
        obj.addProperty(KEY_PERSISTENT, persistent);
        obj.addProperty(KEY_DOMAIN, cookie.domain);
        obj.addProperty(KEY_PATH, cookie.path);
        obj.addProperty(KEY_SECURE, cookie.secure);
        obj.addProperty(KEY_HTTP_ONLY, cookie.httpOnly);
        obj.addProperty(KEY_HOST_ONLY, cookie.hostOnly);
    }
}