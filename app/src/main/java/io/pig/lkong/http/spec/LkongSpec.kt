package io.pig.lkong.http.spec

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.*
import io.pig.lkong.http.data.req.ForumReq
import io.pig.lkong.http.data.req.SignReq
import io.pig.lkong.http.data.resp.ForumResp
import io.pig.lkong.http.data.resp.RespBase
import io.pig.lkong.http.data.resp.SignResp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 龙空接口
 *
 * @author yinhang
 * @since 2021/6/16
 */
interface LkongSpec {

    @GET(RestApiConst.GET_FAVORITE_URL)
    suspend fun getFavorite(): LkongForumThreadResp

    @GET(RestApiConst.GET_HOST)
    suspend fun getHot(): LkongHotThreadResp

    @POST(RestApiConst.SIGN_URL)
    fun signIn(@Body body: RequestBody): Call<LkongSignResp>

    @GET(RestApiConst.BASE_API)
    suspend fun getPostList(
        @Query("sars") sars: String,
        @Query("mod") mod: String
    ): LkongPostListResp

    @POST("/graphql")
    suspend fun signIn(@Body signReq: SignReq): RespBase<SignResp>

    @POST("/graphql")
    suspend fun getForums(@Body forumReq: ForumReq): RespBase<ForumResp>
}