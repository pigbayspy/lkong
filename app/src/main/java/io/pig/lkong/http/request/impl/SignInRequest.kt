package io.pig.lkong.http.request.impl

import com.google.gson.JsonParser
import io.pig.lkong.http.data.LkongAuthResp
import io.pig.lkong.http.request.AbstractHttpRequest
import io.pig.lkong.http.util.CookieUtil
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

    override fun buildRequest(): Request {
        val formBody: FormBody = FormBody.Builder()
            .add("action", "login")
            .add("email", userEmail)
            .add("password", userPassword)
            .add("rememberme", "on")
            .build()
        return Request.Builder()
            .url("http://lkong.cn/index.php?mod=login")
            .post(formBody)
            .build()
    }

    override fun parseResponse(response: Response): LkongAuthResp? {
        val body = response.body?.string() ?: return null
        val json = JsonParser.parseString(body).asJsonObject
        if (json.has("error")) {
            return null
        }

        // 获取 cookie
        val authCookie = getAuthToken()

        return LkongAuthResp(
            uid = json.get("uid").asLong,
            name = json.get("name").asString,
            success = json.get("success").asBoolean,
            yoosuu = json.get("yousuu").asString,
            authCookie = authCookie
        )
    }

    private fun getAuthToken(): String {
        for ((uri, cookies) in handler.getCookies()) {
            for (cookie in cookies) {
                if (cookie.name.equals("auth", true)) {
                    if (CookieUtil.hasExpired(cookie)) {
                        continue
                    }
                    return CookieUtil.encode(uri.toString(), cookie)
                }
            }
        }
        return ""
    }

}