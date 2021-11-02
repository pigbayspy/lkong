package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData
import io.pig.lkong.http.data.resp.data.common.ForumData

class SearchThreadRespData(
    val searchAfter: String,
    val hasMore: Boolean,
    val threads: List<ThreadItem>,
) {
    class ThreadItem(
        val tid: Long,
        val title: String,
        val author: AuthorData,
        val dateline: Long,
        val replies: Int,
        val views: Int,
        val forum: ForumData
    )
}