package io.pig.lkong.http.spec

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.data.LkongHotThreadResp
import io.pig.lkong.http.data.LkongPostListResp
import io.pig.lkong.http.data.LkongSignResp
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
}