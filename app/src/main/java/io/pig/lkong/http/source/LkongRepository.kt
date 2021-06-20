package io.pig.lkong.http.source

import io.pig.lkong.http.data.LkongForumThreadResp
import io.pig.lkong.http.provider.LkongServiceProvider

/**
 * @author yinhang
 * @since 2021/6/20
 */
object LkongRepository {

    private val lkongSpec = LkongServiceProvider.lkongClient

    suspend fun getFavoriteThread(): LkongForumThreadResp {
        return lkongSpec.getFavorite()
    }
}