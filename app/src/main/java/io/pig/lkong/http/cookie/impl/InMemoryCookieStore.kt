package io.pig.lkong.http.cookie.impl

import io.pig.lkong.http.CookieManager
import okhttp3.Cookie
import okhttp3.HttpUrl


/**
 * @author yinhang
 * @since 2021/5/17
 */
class InMemoryCookieStore : CookieManager {

    private val store = mutableMapOf<HttpUrl, Set<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val value = store[url] ?: return emptyList()
        return value.toList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val value = store[url]
        if (value != null) {
            store[url] = value + cookies.toSet()
        } else {
            store[url] = cookies.toSet()
        }
    }

    override fun clear() {
        store.clear()
    }

    override fun getAll(): Map<HttpUrl, Set<Cookie>> {
        return store
    }
}