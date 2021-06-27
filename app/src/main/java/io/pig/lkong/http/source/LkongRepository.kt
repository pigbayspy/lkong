package io.pig.lkong.http.source

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.*
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

    suspend fun getHot(): LkongHotThreadResp {
        return lkongSpec.getHot()
    }

    suspend fun getPostList(thread: Long, page: Int): LkongPostListResp {
        val postListReq = LkongPostListReq(thread, page)
        return lkongSpec.getPostList(postListReq.sars, postListReq.mod)
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
        for (cookies in cookieManager.getAll()) {
            for (cookie in cookies.value) {
                if (cookie.name.equals(key, true)) {
                    if (CookieUtil.hasExpired(cookie)) {
                        continue
                    }
                    return CookieUtil.encode(RestApiConst.LKONG_HOST, cookie)
                }
            }
        }
        return ""
    }
}