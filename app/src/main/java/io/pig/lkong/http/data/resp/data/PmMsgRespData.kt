package io.pig.lkong.http.data.resp.data

class PmMsgRespData(
    val data: List<PmMsgItem>,
    val hasMore: Boolean,
    val nextDate: Long
) {

    class PmMsgItem(
        val content: String,
        val dateline: Long,
        val id: String,
        val uid: Long
    )
}