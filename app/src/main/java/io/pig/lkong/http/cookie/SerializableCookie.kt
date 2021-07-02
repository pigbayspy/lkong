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
    private const val KEY_HTTPONLY = "httpOnly"
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

        if (obj.get(KEY_HTTPONLY).asBoolean) {
            builder.httpOnly()
        }

        val cookie = builder.build()
        return (url.toHttpUrl() to cookie)
    }
}