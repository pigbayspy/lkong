package io.pig.lkong.http.data.resp.data

class UserThreadRespData(
    val fid: Long,
    val tid: Long,
    val dateline: Long,
    val replies: Int,
    val title: String,
    val status: String,
    val lock: Boolean
)