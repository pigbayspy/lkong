package io.pig.lkong.http.source

import io.pig.lkong.http.const.RestApiConst
import io.pig.lkong.http.data.*
import io.pig.lkong.http.data.req.ForumReq
import io.pig.lkong.http.data.req.SignReq
import io.pig.lkong.http.data.req.HotThreadReq
import io.pig.lkong.http.data.req.UserProfileReq
import io.pig.lkong.http.data.resp.ForumResp
import io.pig.lkong.http.data.resp.RespBase
import io.pig.lkong.http.data.resp.HotThreadResp
import io.pig.lkong.http.data.resp.UserProfileResp
import io.pig.lkong.http.provider.LkongServiceProvider
import io.pig.lkong.http.util.CookieUtil

/**
 * @author yinhang
 * @since 2021/6/20
 */
object LkongRepository {

    private val lkongSpec = LkongServiceProvider.lkongClient

    suspend fun getFavoriteThread(): LkongForumThreadResp {
        return lkongSpec.getFavorite()
    }

    suspend fun getUserProfile():RespBase<UserProfileResp> {
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
            return LkongSignInResp(
                body.data.login.name,
                body.data.login.uid,
                success = true
            )
        }
        return LkongSignInResp(success = false)
    }
}