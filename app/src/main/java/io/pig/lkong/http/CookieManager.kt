package io.pig.lkong.http

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @author yinhang
 * @since 2021/5/17
 */
interface CookieManager : CookieJar {

    fun clear()

    fun getAll(): Map<HttpUrl, Set<Cookie>>

    fun get(url: HttpUrl): Set<Cookie>
}