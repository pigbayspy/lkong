package io.pig.lkong.http.source

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.LkongAuthResp
import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.data.LkongSignReq
import io.pig.lkong.http.provider.LkongServiceProvider
import io.pig.lkong.http.util.CookieUtil
import okhttp3.MultipartBody

/**
 * @author yinhang
 * @since 2021/6/20
 */
object LkongRepository {

    private val lkongSpec = LkongServiceProvider.lkongClient

    private val cookieManager = LkongServiceProvider.lkongCookie

    suspend fun getFavoriteThread(): LkongForumThreadResp {
        return lkongSpec.getFavorite()
    }

    fun signIn(signInReq: LkongSignReq): LkongAuthResp {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("action", signInReq.action)
            .addFormDataPart("email", signInReq.email)
            .addFormDataPart("password", signInReq.password)
            .addFormDataPart("rememberme", signInReq.rememberMe)
            .build()
        val response = lkongSpec.signIn(requestBody).execute()
        val responseBody = response.body()!!
        val cookie = getCookie("auth")
        return LkongAuthResp(
            responseBody.name,
            responseBody.uid,
            responseBody.yousuu,
            responseBody.success,
            cookie
        )
    }

    private fun getCookie(key: String): String {
        for (cookie in cookieManager.get(RestApiConst.Lkong_URl)) {
            if (cookie.name.equals(key, true)) {
                if (CookieUtil.hasExpired(cookie)) {
                    continue
                }
                return CookieUtil.encode(RestApiConst.LKONG_HOST, cookie)
            }
        }
        return ""
    }
}