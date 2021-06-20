package io.pig.lkong.http.spec

import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.data.LkongSignResp
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 龙空接口
 *
 * @author yinhang
 * @since 2021/6/16
 */
interface LkongSpec {

    @GET("/index.php?mod=data&sars=my/favorite")
    suspend fun getFavorite(): LkongForumThreadResp

    @POST("/index.php?mod=login")
    suspend fun signIn(@Body body: RequestBody): Response<LkongSignResp>
}