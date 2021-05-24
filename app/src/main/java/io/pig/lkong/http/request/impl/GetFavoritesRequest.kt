package io.pig.lkong.http.request.impl

import com.google.gson.Gson
import io.pig.lkong.http.const.RestApiConst.GET_FAVORITE_URL
import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.request.AbstractHttpRequest
import okhttp3.Request
import okhttp3.Response

/**
 * @author yinhang
 * @since 2021/5/24
 */
class GetFavoritesRequest(private val start: Long) : AbstractHttpRequest<LkongForumThreadResp>() {

    private val gson = Gson()

    override fun parseResponse(response: Response): LkongForumThreadResp? {
        val body = response.body?.string() ?: return null
        val resp = gson.fromJson(body, LkongForumThreadResp::class.java)
        return resp
    }

    override fun buildRequest(): Request {
        val url = if (start > 0) {
            "$GET_FAVORITE_URL&nexttime=$start"
        } else GET_FAVORITE_URL
        return Request.Builder().url(url).build()
    }
}