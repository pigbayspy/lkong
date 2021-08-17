package io.pig.lkong.http.spec

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.req.*
import io.pig.lkong.http.data.resp.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 龙空接口
 *
 * @author yinhang
 * @since 2021/6/16
 */
interface LkongSpec {

    @POST(RestApiConst.GRAPHQL)
    suspend fun getFavorite(@Body favoriteReq: FavoriteReq): RespBase<FavoriteResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getThreadPost(@Body threadPostReq: ThreadPostReq): RespBase<ThreadPostResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun signIn(@Body signReq: SignReq): Response<RespBase<SignResp>>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getForums(@Body forumReq: ForumReq): RespBase<ForumResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getHot(@Body hotReq: HotThreadReq): RespBase<HotThreadResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getUserProfile(@Body profileReq: UserProfileReq): RespBase<UserProfileResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getTimeline(@Body timelineReq: TimelineReq): RespBase<TimelineResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getCollections(@Body collectionReq: CollectionReq): RespBase<CollectionResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getForumThreads(@Body forumThreadReq: ForumThreadReq): RespBase<ForumThreadResp>

}