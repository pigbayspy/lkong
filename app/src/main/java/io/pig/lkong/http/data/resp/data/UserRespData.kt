package io.pig.lkong.http.data.resp.data

/**
 * @author yinhang
 * @since 2021/7/16
 */
class UserRespData(
    val uid: Long,
    val name: String,
    val avatar: String?,
    val verify: String?,
    val status: String?,
    val dateline: Long
)