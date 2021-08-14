package io.pig.lkong.http.source

import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.data.LkongPostListReq
import io.pig.lkong.http.data.LkongPostListResp
import io.pig.lkong.http.data.LkongSignInResp
import io.pig.lkong.http.data.req.*
import io.pig.lkong.http.data.resp.*
import io.pig.lkong.http.provider.LkongServiceProvider
import io.pig.lkong.http.util.CookieUtil

/**
 * @author yinhang
 * @since 2021/6/20
 */
object LkongRepository {

    private val lkongSpec = LkongServiceProvider.lkongClient

    private val lkongCookie = LkongServiceProvider.lkongCookie

    suspend fun getFavoriteThread(): LkongForumThreadResp {
        return lkongSpec.getFavorite()
    }

    suspend fun getUserProfile(): RespBase<UserProfileResp> {
        val req = UserProfileReq()
        return lkongSpec.getUserProfile(req)
    }

    suspend fun getHot(): RespBase<HotThreadResp> {
        val req = HotThreadReq()
        return lkongSpec.getHot(req)
    }

    suspend fun getPostList(thread: Long, page: Int): LkongPostListResp {
        val postListReq = LkongPostListReq(thread, page)
        return lkongSpec.getPostList(postListReq.sars, postListReq.mod)
    }

    suspend fun getForums(): RespBase<ForumResp> {
        val forumReq = ForumReq()
        return lkongSpec.getForums(forumReq)
    }

    suspend fun signIn(email: String, password: String): LkongSignInResp {
        val signReq = SignReq(email, password)
        val response = lkongSpec.signIn(signReq)
        val body = response.body()
        if (response.isSuccessful && body != null && body.data != null) {
            val authCookie = getCookie("EGG_SESS")
            return LkongSignInResp(
                body.data.login.name,
                body.data.login.uid,
                success = true,
                authCookie,
                body.data.login.avatar
            )
        }
        return LkongSignInResp(success = false)
    }

    suspend fun getCollections(uid: Long): RespBase<CollectionResp> {
        val req = CollectionReq(uid)
        return lkongSpec.getCollections(req)
    }

    suspend fun getTimeline(nextTime: Long): RespBase<TimelineResp> {
        val req = TimelineReq(nextTime)
        return lkongSpec.getTimeline(req)
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