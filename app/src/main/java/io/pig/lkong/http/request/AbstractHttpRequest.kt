package io.pig.lkong.http.request

import io.pig.lkong.http.handler.HttpHandler
import io.pig.lkong.http.handler.impl.DefaultHttpHandler
import okhttp3.Request
import okhttp3.Response

/**
 * @author yinhang
 * @since 2021/5/17
 */
abstract class AbstractHttpRequest<T>(private val handler: HttpHandler = DefaultHttpHandler) {

    protected abstract fun parseResponse(response: Response): T
    protected abstract fun buildRequest(): Request

    protected open fun onPreExecute() {}

    protected open fun onPostExecute() {
        clearCookies()
    }

    protected open fun executeHttpRequest(): Response {
        return handler.newCall(buildRequest()).execute()
    }

    fun execute(): T {
        onPreExecute()
        val response: Response = executeHttpRequest()
        val result: T = parseResponse(response)
        onPostExecute()
        return result
    }

    private fun clearCookies() {
        handler.clearCookies()
    }
}