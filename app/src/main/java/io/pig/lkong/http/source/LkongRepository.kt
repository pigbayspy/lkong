package io.pig.lkong.http.source

import io.pig.lkong.http.data.req.CollectionReq
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
import io.pig.lkong.http.data.resp.SignInResp
import io.pig.lkong.http.data.resp.ThreadPostResp
import io.pig.lkong.http.data.resp.TimelineResp
import io.pig.lkong.http.data.resp.UserProfileResp
import io.pig.lkong.http.provider.LkongServiceProvider
import io.pig.lkong.http.util.CookieUtil

/**
 * @author yinhang
 * @since 2021/6/20
 */
object LkongRepository {

    private val lkongSpec = LkongServiceProvider.lkongClient

    private val lkongCookie = LkongServiceProvider.lkongCookie

    suspend fun getFavorites(uid: Long): RespBase<FavoriteResp> {
        val req = FavoriteReq(uid)
        return lkongSpec.getFavorite(req)
    }

    suspend fun getUserProfile(uid: Long): RespBase<UserProfileResp> {
        val req = UserProfileReq(uid)
        return lkongSpec.getUserProfile(req)
    }

    suspend fun getHot(): RespBase<HotThreadResp> {
        val req = HotThreadReq()
        return lkongSpec.getHot(req)
    }

    suspend fun getThreadPost(tid: Long, page: Int = 1): RespBase<ThreadPostResp> {
        val threadPostReq = ThreadPostReq(tid, page)
        return lkongSpec.getThreadPost(threadPostReq)
    }

    suspend fun getForums(): RespBase<ForumResp> {
        val forumReq = ForumReq()
        return lkongSpec.getForums(forumReq)
    }

    suspend fun signIn(email: String, password: String): SignInResp {
        val signReq = SignReq(email, password)
        val response = lkongSpec.signIn(signReq)
        val body = response.body()
        if (response.isSuccessful && body != null && body.data != null) {
            val authCookie = getCookie("EGG_SESS")
            return SignInResp(
                body.data.login.name,
                body.data.login.uid,
                success = true,
                authCookie,
                body.data.login.avatar
            )
        }
        return SignInResp(success = false)
    }

    suspend fun getCollections(uid: Long): RespBase<CollectionResp> {
        val req = CollectionReq(uid)
        return lkongSpec.getCollections(req)
    }

    suspend fun getTimeline(nextTime: Long): RespBase<TimelineResp> {
        val req = TimelineReq(nextTime)
        return lkongSpec.getTimeline(req)
    }

    suspend fun getForumThread(forum: Long, page: Int): RespBase<ForumThreadResp> {
        val req = ForumThreadReq(forum, page)
        return lkongSpec.getForumThreads(req)
    }

    suspend fun getNotice(): RespBase<NoticeResp> {
        val req = NoticeReq()
        return lkongSpec.getNotice(req)
    }

    suspend fun punch(): RespBase<PunchResp> {
        val req = PunchReq()
        return lkongSpec.punch(req)
    }

    private fun getCookie(key: String): String {
        for (cookie in lkongCookie.getAll()) {
            if (cookie.name.equals(key, true)) {
                if (CookieUtil.hasExpired(cookie)) {
                    continue
                }
                return CookieUtil.encode(cookie)
            }
        }

        return ""
    }
}