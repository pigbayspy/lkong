package io.pig.lkong.http.spec

import io.pig.lkong.http.data.LkongForumThreadResp
import retrofit2.http.GET

/**
 * 龙空接口
 *
 * @author yinhang
 * @since 2021/6/16
 */
interface LkongSpec {

    @GET("/index.php?mod=data&sars=my/favorite")
    suspend fun getFavorite(): LkongForumThreadResp
}