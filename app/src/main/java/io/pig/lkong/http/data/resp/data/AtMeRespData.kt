package io.pig.lkong.http.data.resp.data

class AtMeRespData(
    val hasMore: Boolean,
    val nextDate: Long,
    val data: List<TimelineItemData?>
)