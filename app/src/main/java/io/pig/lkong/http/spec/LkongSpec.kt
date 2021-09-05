package io.pig.lkong.http.spec

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.req.CollectionReq
import io.pig.lkong.http.data.req.FansReq
import io.pig.lkong.http.data.req.FavoriteReq
import io.pig.lkong.http.data.req.ForumReq
import io.pig.lkong.http.data.req.ForumThreadReq
import io.pig.lkong.http.data.req.HotThreadReq
import io.pig.lkong.http.data.req.NoticeReq
import io.pig.lkong.http.data.req.PunchReq
import io.pig.lkong.http.data.req.SignReq
import io.pig.lkong.http.data.req.ThreadPostReq
import io.pig.lkong.http.data.req.TimelineReq
import io.pig.lkong.http.data.req.UserProfileReq
import io.pig.lkong.http.data.resp.CollectionResp
import io.pig.lkong.http.data.resp.FavoriteResp
import io.pig.lkong.http.data.resp.ForumResp
import io.pig.lkong.http.data.resp.ForumThreadResp
import io.pig.lkong.http.data.resp.HotThreadResp
import io.pig.lkong.http.data.resp.NoticeResp
import io.pig.lkong.http.data.resp.PunchResp
import io.pig.lkong.http.data.resp.RespBase
import io.pig.lkong.http.data.resp.SignResp
import io.pig.lkong.http.data.resp.ThreadPostResp
import io.pig.lkong.http.data.resp.TimelineResp
import io.pig.lkong.http.data.resp.UserProfileResp
import io.pig.lkong.http.data.resp.data.FansResp
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

    @POST(RestApiConst.GRAPHQL)
    suspend fun getNotice(@Body noticeReq: NoticeReq): RespBase<NoticeResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun punch(@Body punchReq: PunchReq): RespBase<PunchResp>

    @POST(RestApiConst.GRAPHQL)
    suspend fun getFans(@Body fansReq: FansReq): RespBase<FansResp>
}