package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/8/15
 */
class ForumThreadRespData(
    val tid: Long,
    val fid: Long,
    val title: String,
    val status: String,
    val lock: Boolean,
    val replies: Int,
    val views: Int,
    val dateline: Long,
    val lastpost: Long,
    val highlight: Int,
    val author: AuthorData
) {
    data class AuthorData(
        val name: String,
        val uid: Long,
        val avatar: String
    )
}