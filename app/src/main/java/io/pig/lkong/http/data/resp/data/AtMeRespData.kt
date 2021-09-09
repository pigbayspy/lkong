package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData
import io.pig.lkong.http.data.resp.data.common.QuoteData
import io.pig.lkong.http.data.resp.data.common.ThreadData

class AtMeRespData(
    val hasMore: Boolean,
    val nextDate: Long,
    val data: List<AtMeItemData>
) {
    class AtMeItemData(
        val author: AuthorData,
        val dateline: Long,
        val content: String,
        val pid: String,
        val quote:QuoteData?,
        val thread:ThreadData?
    )
}