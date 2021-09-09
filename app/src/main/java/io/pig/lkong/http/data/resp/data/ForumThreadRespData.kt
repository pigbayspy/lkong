package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData

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
    val digest: List<String>?,
    val dateline: Long,
    val lastpost: Long,
    val highlight: Int,
    val author: AuthorData
)