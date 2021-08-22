package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/21
 */
class TimelineItemData(
    val author: AuthorData,
    val dateline: Long,
    val content: String,
    val pid: String,
    val quote: TimelineQuoteData?,
    val thread: TimelineThreadData
) {
    data class AuthorData(
        val uid: Long,
        val name: String,
        val avatar: String?
    )

    data class TimelineQuoteData(
        val pid: String,
        val content: String,
        val author: AuthorData
    )

    class TimelineThreadData(
        val tid: Long,
        val title: String,
        val replies: Int?,
        val fid: Int?,
        val forumName: String?,
        val author: AuthorData?
    )
}