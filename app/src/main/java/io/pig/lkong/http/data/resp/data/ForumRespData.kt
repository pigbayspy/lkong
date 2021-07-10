package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/10
 */
data class ForumRespData(
    val type: String,
    val name: String,
    val fid: Long,
    val num: Long,
    val link: String?,
    val __typename: String
)