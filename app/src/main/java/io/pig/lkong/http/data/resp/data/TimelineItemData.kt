package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/21
 */
class TimelineItemData(
    val authorid: Long,
    val author: AuthorData,
    val dateline: Long,
    val content: String,
    val pid: String,
    val quote: TimelineQuoteData,
    val thread: TimelineThreadData
)