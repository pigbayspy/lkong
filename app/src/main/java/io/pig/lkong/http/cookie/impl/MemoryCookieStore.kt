package io.pig.lkong.http.cookie.impl

import io.pig.lkong.http.cookie.CookieManager
import okhttp3.Cookie
import okhttp3.HttpUrl


/**
 * @author yinhang
 * @since 2021/5/17
 */
class MemoryCookieStore : CookieManager {

    private val store = PersistentCookieStore()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return this.store.get(url)
    }

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.store.add(url, cookies);
    }

    @Synchronized
    override fun clear() {
        this.store.removeAll()
    }

    override fun getAll(): List<Cookie> {
        return this.store.getCookies()
    }
}