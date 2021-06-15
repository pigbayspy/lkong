package io.pig.lkong.http.handler.impl

import io.pig.lkong.http.CookieManager
import io.pig.lkong.http.cookie.impl.InMemoryCookieStore
import io.pig.lkong.http.handler.HttpHandler
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * @author yinhang
 * @since 2021/5/17
 */
object DefaultHttpHandler : HttpHandler {

    private val cookieJar: CookieManager = InMemoryCookieStore()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .build()

    override fun newCall(request: Request): Call {
        return httpClient.newCall(request)
    }

    override fun clearCookies() {
        cookieJar.clear()
    }

    override fun getCookies(): Map<HttpUrl, Set<Cookie>> {
        return cookieJar.getAll()
    }
}