package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/22
 */
class TimelineThreadData(
    val tid: Long,
    val title: String,
    val replies: Int?,
    val fid: Int?,
    val forumName: String?,
    val author: AuthorData?
)