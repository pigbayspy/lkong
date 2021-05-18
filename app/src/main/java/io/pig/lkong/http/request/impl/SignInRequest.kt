package io.pig.lkong.http.request.impl

import com.google.gson.Gson
import io.pig.lkong.http.data.LkongAuthResp
import io.pig.lkong.http.request.AbstractHttpRequest
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response

/**
 * @author yinhang
 * @since 2021/5/18
 */
class SignInRequest(
    private val userEmail: String,
    private val userPassword: String
) : AbstractHttpRequest<LkongAuthResp>() {

    companion object {
        private val gson = Gson()
    }

    override fun buildRequest(): Request {
        val formBody: FormBody = FormBody.Builder()
            .add("action", "login")
            .add("email", userEmail)
            .add("password", userPassword)
            .add("rememberme", "on")
            .build()
        return Request.Builder()
            .addHeader("Accept-Encoding", "gzip")
            .url("http://lkong.cn/index.php?mod=login")
            .post(formBody)
            .build()
    }

    override fun parseResponse(response: Response): LkongAuthResp? {
        val body = response.body?.string() ?: return null
        return gson.fromJson(body, LkongAuthResp::class.java)
    }

}