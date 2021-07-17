package io.pig.lkong.http.cookie

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStore {
    /**
     * 添加cookie
     */
    fun add(httpUrl: HttpUrl, cookie: Cookie)

    /**
     * 添加指定 HttpUrl, Cookie 集合
     */
    fun add(httpUrl: HttpUrl, cookies: List<Cookie>)

    /**
     * 根据 HttpUrl从缓存中读取cookie集合
     */
    fun get(httpUrl: HttpUrl): List<Cookie>

    /**
     * 获取全部缓存cookie
     */
    fun getCookies(): List<Cookie>

    /**
     * 移除指定httpurl cookie集合
     */
    fun remove(httpUrl: HttpUrl, cookie: Cookie): Boolean

    /**
     * 移除所有cookie
     */
    fun removeAll()
}