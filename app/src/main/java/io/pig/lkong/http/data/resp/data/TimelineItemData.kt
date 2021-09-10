package io.pig.lkong.http.data.resp.data

import io.pig.lkong.http.data.resp.data.common.AuthorData
import io.pig.lkong.http.data.resp.data.common.QuoteData
import io.pig.lkong.http.data.resp.data.common.ThreadData

/**
 * @author yinhang
 * @since 2021/7/21
 */
class TimelineItemData(
    val author: AuthorData,
    val dateline: Long,
    val content: String,
    val pid: String,
    val quote: QuoteData?,
    val thread: ThreadData
)