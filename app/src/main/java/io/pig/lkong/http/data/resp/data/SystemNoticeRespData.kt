package io.pig.lkong.http.data.resp.data

class SystemNoticeRespData(
    val data: List<SystemNoticeItem>,
    val hasMore: Boolean,
    val nextDate: Long
) {
    class SystemNoticeItem(
        val id: String,
        val uid: Long,
        val action: String,
        val content: String,
        val authorid: Long,
        val author: String,
        val dateline: Long,
        val fromid: Long
    )
}