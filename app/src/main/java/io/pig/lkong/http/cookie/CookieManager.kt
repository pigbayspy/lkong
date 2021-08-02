package io.pig.lkong.http.cookie

import okhttp3.Cookie
import okhttp3.CookieJar

/**
 * @author yinhang
 * @since 2021/5/17
 */
interface CookieManager : CookieJar {

    fun clear()

    fun getAll(): List<Cookie>
}