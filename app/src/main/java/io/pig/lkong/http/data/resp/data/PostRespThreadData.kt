package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData
import io.pig.lkong.http.data.resp.data.common.ForumData

class PostRespThreadData(
    val title: String,
    val tid: Long,
    val forum: ForumData,
    val replies: Int,
    val views: Int,
    val status: String,
    val lock: Boolean,
    val first: String,
    val digest: List<String?>?,
    val dateline: Long,
    val lastpost: Long,
    val author: AuthorData
)