package io.pig.lkong.http.handler

import okhttp3.Call
import okhttp3.Request

/**
 * @author yinhang
 * @since 2021/5/17
 */
interface HttpHandler {

    /**
     * 请求数据
     */
    fun newCall(request: Request): Call

    fun clearCookies()
}