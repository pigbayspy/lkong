package io.pig.lkong.http.data

/**
 * @author yinhang
 * @since 2021/7/4
 */
data class LkongForumInfoResp(
    val fid: Long,
    val name: String,
    val description: String,
    val status: String,
    val sortbydateline: Int,
    val threads: String,
    val todayposts: String,
    val fansnum: Int,
    val blackboard: String ,
    val moderators: Array<String>
)