package io.pig.lkong.http.data.resp.data.common

class ThreadData(
    val tid: Long,
    val title: String,
    val replies: Int?,
    val fid: Int?,
    val forumName: String?,
    val author: AuthorData?
)