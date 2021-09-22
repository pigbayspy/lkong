package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData

class PrivateMsgListRespData(
    val data: List<PrivateMsgItem>,
    val hasMore: Boolean,
    val nextDate: Long
) {

    class PrivateMsgItem(
        val user: AuthorData,
        val content: String,
        val lastTime: Long,
        val newCount: Int
    )
}