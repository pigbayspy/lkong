package io.pig.lkong.http.data

/**
 * 返回的帖子数据
 *
 * @author yinhang
 * @since 2021/5/24
 */
data class LkongForumItemResp(
    var sortkey: Long,
    val dateline: String,
    val subject: String,
    val username: String,
    val digest: Int,
    val closed: Int,
    val uid: Long,
    val replynum: Int,
    val id: String,
    val fid: Long
)