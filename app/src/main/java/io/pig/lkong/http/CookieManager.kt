package io.pig.lkong.http

import okhttp3.CookieJar

/**
 * @author yinhang
 * @since 2021/5/17
 */
interface CookieManager : CookieJar {

    fun clear()
}