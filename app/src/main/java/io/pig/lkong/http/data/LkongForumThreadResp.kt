package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/5/24
 */
data class LkongForumThreadResp(
    val nexttime: Long,
    val tmp: String,
    val data: List<LkongForumItemResp>,
    val nochecknew: Boolean
)