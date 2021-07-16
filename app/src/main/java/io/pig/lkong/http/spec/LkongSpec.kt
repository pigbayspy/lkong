package io.pig.lkong.http.spec

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.data.LkongPostListResp
import io.pig.lkong.http.data.req.ForumReq
import io.pig.lkong.http.data.req.HotThreadReq
import io.pig.lkong.http.data.req.SignReq
import io.pig.lkong.http.data.req.UserProfileReq
import io.pig.lkong.http.data.resp.*
import retrofit2.Response
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

    @GET(RestApiConst.BASE_API)
    suspend fun getPostList(
        @Query("sars") sars: String,
        @Query("mod") mod: String
    ): LkongPostListResp

    @POST("/graphql")
    suspend fun signIn(@Body signReq: SignReq): Response<RespBase<SignResp>>

    @POST("/graphql")
    suspend fun getForums(@Body forumReq: ForumReq): RespBase<ForumResp>

    @POST("/graphql")
    suspend fun getHot(@Body hotReq: HotThreadReq): RespBase<HotThreadResp>

    @POST("/graphql")
    suspend fun getUserProfile(@Body profileReq: UserProfileReq): RespBase<UserProfileResp>
}